package com.nhnacademy.taskapi.payment.controller;

import com.nhnacademy.taskapi.payment.dto.CheckoutInfoResponse;
import com.nhnacademy.taskapi.payment.dto.PaymentRequest;
import com.nhnacademy.taskapi.payment.dto.PaymentResponse;
import com.nhnacademy.taskapi.payment.dto.toss.TossConfirmRequest;
import com.nhnacademy.taskapi.payment.dto.toss.TossConfirmResponse;
import com.nhnacademy.taskapi.payment.exception.PaymentNotFoundException;
import com.nhnacademy.taskapi.payment.service.PaymentService;
import com.nhnacademy.taskapi.payment.service.impl.TossPaymentServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "결제, 결제조회, 결제 승인등 결제 관련 기능")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final TossPaymentServiceImpl tossPaymentService; // Toss 전용 결제 승인 로직

    /**
     * 결제 생성(READY)
     * 기존에는 @PathVariable Long orderId 이었지만
     * 토스 결제 재시도 시 orderId가 "123456789_random" 형태로 들어올 수 있으므로
     * String으로 받고, 언더바 전까지만 파싱해서 DB조회는 서비스 계층에서 처리
     */
    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> createPayment(
            @PathVariable("orderId") String orderIdStr,          // String
            @RequestHeader("X-MEMBER-ID") Long memberId,
            @RequestBody PaymentRequest paymentRequest
    ) {
        log.info("Create Payment for orderId={}, memberId={}", orderIdStr, memberId);

        // 결제 생성
        PaymentResponse response = paymentService.createPayment(orderIdStr, memberId, paymentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 결제 단건 조회
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long paymentId) {
        PaymentResponse payment = paymentService.getPayment(paymentId);
        return ResponseEntity.ok(payment);
    }


    // ------------------------------------------------------------------------
    // ★ 토스 결제 승인 ★
    // ------------------------------------------------------------------------
    /**
     * ex) [POST] /task/payments/toss/confirm
     *  - 프론트에서 paymentKey, orderId, amount를 받아와 토스 승인 API 호출 (결제 최종승인)
     *  - orderId 역시 "123456789_random" 형태 가능
     */
    @PostMapping("/toss/confirm")
    public ResponseEntity<TossConfirmResponse> confirmToss(
            @RequestHeader("X-MEMBER-ID") Long memberId,
            @RequestBody TossConfirmRequest request
    ) {
        log.info("Toss 결제 승인 요청: memberId={}, paymentKey={}, orderId={}, amount={}",
                memberId, request.getPaymentKey(), request.getOrderId(), request.getAmount());

        // TossPaymentServiceImpl 내에서 orderIdStr을 처리하도록 유지
        TossConfirmResponse confirmResponse = tossPaymentService.confirmTossPayment(request);
        return ResponseEntity.ok(confirmResponse);
    }

    /**
     * 체크아웃 시 필요한 정보 (주문금액, 포인트 등) 조회
     * (Front에서 결제창 열기 전에 이 API를 호출하여 금액·포인트 확인)
     *
     * 기존: @PathVariable Long orderId
     * 수정: @PathVariable String orderIdStr
     */
    @GetMapping("/checkout-info/{orderId}")
    public ResponseEntity<CheckoutInfoResponse> getCheckoutInfo(
            @PathVariable("orderId") String orderIdStr,
            @RequestHeader("X-MEMBER-ID") Long memberId
    ) {
        // 결제 정보 조회
        CheckoutInfoResponse checkoutInfo = paymentService.getCheckoutInfo(orderIdStr, memberId);
        return ResponseEntity.ok(checkoutInfo);
    }
}
