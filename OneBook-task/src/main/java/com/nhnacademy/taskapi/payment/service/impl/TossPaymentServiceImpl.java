package com.nhnacademy.taskapi.payment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.order.service.OrderService;
import com.nhnacademy.taskapi.payment.domain.Payment;
import com.nhnacademy.taskapi.payment.domain.PaymentMethod;
import com.nhnacademy.taskapi.payment.dto.toss.TossConfirmRequest;
import com.nhnacademy.taskapi.payment.dto.toss.TossConfirmResponse;
import com.nhnacademy.taskapi.payment.exception.PaymentNotFoundException;
import com.nhnacademy.taskapi.payment.repository.PaymentRepository;
import com.nhnacademy.taskapi.payment.service.CommonPaymentService;
import com.nhnacademy.taskapi.point.jpa.JpaPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 결제 승인 시점(DONE)에 포인트 차감
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TossPaymentServiceImpl {
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final RestTemplate restTemplate;

    // 토스 결제 승인시 응답을 JSON 문자열로 받고
    // 이를 Map<String, Object>로 변환하여 쉽게 접근할 수 있도록 함
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TOSS_SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
    private final CommonPaymentService commonPaymentService; // 새로 주입

    @Transactional
    public TossConfirmResponse confirmTossPayment(TossConfirmRequest request) {
        String orderIdStr = request.getOrderId();
        Long realOrderId = parseOrderId(orderIdStr);

        // 1) Payment 조회
        Payment payment = paymentRepository.findByOrder_OrderId(realOrderId);
        if (payment == null) {
            throw new PaymentNotFoundException("해당 주문의 결제 정보가 없습니다.");
        }
        if (!"READY".equals(payment.getStatus())) {
            throw new IllegalStateException("이미 결제완료(DONE) 혹은 취소 상태입니다.");
        }

        // 2) 토스 결제승인 API
        String base64Secret = Base64.getEncoder()
                .encodeToString((TOSS_SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + base64Secret);

        // 응답을 영어로 받기 위해.. 한글로 받으면 깨짐
        headers.set("Accept-Language", "en-US");

        int approveAmount;
        try {
            approveAmount = Integer.parseInt(request.getAmount());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("유효하지 않은 결제 금액입니다.");
        }

        if (payment.getTotalAmount() != approveAmount) {
            throw new IllegalStateException("승인 요청 금액과 DB 금액이 다릅니다.");
        }

        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", request.getPaymentKey());
        body.put("orderId", request.getOrderId());
        body.put("amount", approveAmount);

        //RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(
                    "https://api.tosspayments.com/v1/payments/confirm",
                    new HttpEntity<>(body, headers),
                    String.class
            );
        } catch (Exception e) {
            log.error("[TaskAPI] 토스 결제 승인 API 실패", e);
            throw new RuntimeException("토스 결제 승인 API 요청 실패", e);
        }

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("토스 결제승인 실패: " + responseEntity.getBody());
        }

        Map<String, Object> tossRes;
        try {
            tossRes = objectMapper.readValue(responseEntity.getBody(), Map.class);
        } catch (Exception e) {
            throw new RuntimeException("응답 JSON 파싱 오류: " + responseEntity.getBody());
        }

        String status = (String) tossRes.get("status");      // "DONE"
        String approvedAtStr = (String) tossRes.get("approvedAt");
        String methodStr = (String) tossRes.get("method");   // "카드", "가상계좌" 등
        Map<String, Object> cardObj = (Map<String, Object>) tossRes.get("card");

        // 3) PaymentMethod는 Payment를 통해 cascade=ALL 저장
        PaymentMethod pm = payment.getPaymentMethod();
        if (pm == null) {
            // PaymentMethod가 비어 있다면 새로 생성
            pm = new PaymentMethod();
            pm.setPaymentKey(request.getPaymentKey());
            payment.setPaymentMethod(pm);
        } else {
            // 이미 PaymentMethod가 있다면, key가 동일한지 확인(혹은 overwrite)
            if (!pm.getPaymentKey().equals(request.getPaymentKey())) {
                // 키가 달라지면 충돌 → 예외처리 or 로직 처리
                throw new IllegalStateException("기존 PaymentMethod와 다른 paymentKey가 들어왔습니다.");
            }
        }

        // PaymentMethod 업데이트
        pm.setType("TOSS");
        pm.setMethod(methodStr);

        if (cardObj != null) {
            pm.setCardOwnerType((String) cardObj.get("ownerType"));
            pm.setCardNumber((String) cardObj.get("number"));
            pm.setCardAmount((Integer) cardObj.get("amount"));
            pm.setCardIssuerCode((String) cardObj.get("issuerCode"));
            pm.setCardAcquirerCode((String) cardObj.get("acquirerCode"));
            pm.setCardIsInterestFree((Boolean) cardObj.get("isInterestFree"));
            pm.setCardType((String) cardObj.get("cardType"));
            pm.setCardApproveNo((String) cardObj.get("approveNo"));
            pm.setCardInstallmentPlanMonths((Integer) cardObj.get("installmentPlanMonths"));
        } else {
            pm.setCardAmount(approveAmount);
            pm.setCardType("CASH");
            pm.setCardOwnerType("PERSONAL");
        }
        // cascade=ALL

        // 4) 결제가 DONE이면 포인트 차감, 적립
        if ("DONE".equals(status)) {
            commonPaymentService.usedPurchasePoint(payment); // 차감
            commonPaymentService.accumulationPurchasePoints(payment); // 적립
            orderService.updateOrderStatus(List.of(realOrderId), "배송전"); // 주문 상태 변경
        }

        // 5) Payment 상태 업데이트
        payment.setStatus(status);
        try {
            payment.setApprovedAt(LocalDateTime.parse(approvedAtStr));
        } catch (DateTimeParseException e) {
            payment.setApprovedAt(LocalDateTime.now());
        }

        // **cascade=ALL** 이므로 Payment만 save() → PaymentMethod 자동 저장/업데이트
        paymentRepository.save(payment);

        // 6) 응답
        TossConfirmResponse result = new TossConfirmResponse();
        result.setPaymentKey(request.getPaymentKey());
        result.setOrderId(orderIdStr);
        result.setStatus(status);
        result.setApprovedAt(payment.getApprovedAt());
        result.setMessage("결제 승인 성공");
        result.setMemberId(payment.getOrder().getMember().getId());

        return result;
    }

    public Long parseOrderId(String orderIdStr) {
        if (orderIdStr == null) {
            throw new PaymentNotFoundException("orderId is null");
        }
        if (orderIdStr.contains("_")) {
            String pureLongPart = orderIdStr.substring(0, orderIdStr.indexOf("_"));
            try {
                return Long.parseLong(pureLongPart);
            } catch (NumberFormatException e) {
                throw new PaymentNotFoundException("유효하지 않은 orderId 형식입니다.");
            }
        } else {
            try {
                return Long.parseLong(orderIdStr);
            } catch (NumberFormatException e) {
                throw new PaymentNotFoundException("유효하지 않은 orderId 형식입니다.");
            }
        }
    }

}

