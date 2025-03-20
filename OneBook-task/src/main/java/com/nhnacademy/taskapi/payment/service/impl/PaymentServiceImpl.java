package com.nhnacademy.taskapi.payment.service.impl;

import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.order.entity.Order;
import com.nhnacademy.taskapi.order.repository.OrderRepository;
import com.nhnacademy.taskapi.order.repository.OrderStatusRepository;
import com.nhnacademy.taskapi.order.service.OrderService;
import com.nhnacademy.taskapi.payment.domain.Payment;
import com.nhnacademy.taskapi.payment.domain.PaymentMethod;
import com.nhnacademy.taskapi.payment.dto.CheckoutInfoResponse;
import com.nhnacademy.taskapi.payment.dto.PaymentRequest;
import com.nhnacademy.taskapi.payment.dto.PaymentResponse;
import com.nhnacademy.taskapi.payment.exception.InsufficientPointException;
import com.nhnacademy.taskapi.payment.exception.InvalidPaymentException;
import com.nhnacademy.taskapi.payment.exception.PaymentNotFoundException;
import com.nhnacademy.taskapi.payment.repository.PaymentRepository;
import com.nhnacademy.taskapi.payment.service.CommonPaymentService;
import com.nhnacademy.taskapi.payment.service.PaymentService;
import com.nhnacademy.taskapi.point.domain.Point;
import com.nhnacademy.taskapi.point.jpa.JpaPointRepository;
import com.nhnacademy.taskapi.point.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final MemberRepository memberRepository;
    private final JpaPointRepository pointRepository;
    private final CommonPaymentService commonPaymentService; // 새로 주입
    private final OrderService orderService;

    @Override
    @Transactional
    public PaymentResponse createPayment(String orderIdStr, Long memberId, PaymentRequest paymentRequest) {
        Long realOrderId = parseOrderId(orderIdStr);

        // 이미 READY 결제가 있는지 확인
        Payment existingPayment = paymentRepository.findByOrder_OrderId(realOrderId);
        if (existingPayment != null) {
            // 이미 Payment가 존재하는 상황
            if ("READY".equals(existingPayment.getStatus())) {
                // 전체 결제 금액
                int orderTotalAmount = existingPayment.getOrder().getTotalPrice() + existingPayment.getOrder().getDeliveryPrice() + existingPayment.getOrder().getPackagingPrice();
                int newUsedPoint = paymentRequest.getUsedPoint();
                int newFinalPayAmount = orderTotalAmount - newUsedPoint;

                // 순수 결제 금액
                int newOnlyBookAmount = existingPayment.getOrder().getTotalPrice() - newUsedPoint;

                if (newOnlyBookAmount < 0) {
                    newOnlyBookAmount = 0;
                }

                if (newFinalPayAmount < 0) {
                    throw new InvalidPaymentException("포인트 사용액이 주문 금액을 초과합니다.");
                }
                existingPayment.setPoint(newUsedPoint);
                existingPayment.setTotalAmount(newFinalPayAmount);
                existingPayment.setOnlyBookAmount(newOnlyBookAmount);

                // 만약 newFinalPayAmount == 0 이면 => 전액 포인트 결제
                if (newFinalPayAmount == 0) {
                    // 즉시 DONE 처리
                    // 기존 handleFullPointPayment() 대신 commonPaymentService 호출
                    commonPaymentService.handleFullPointPayment(existingPayment, memberId);
                }

                paymentRepository.save(existingPayment);
                return buildPaymentResponse(existingPayment, orderIdStr);
            } else {
                throw new InvalidPaymentException("이미 결제된 (DONE 등) 주문입니다.");
            }
        }

        // 주문 존재 여부
        Order order = orderRepository.findById(realOrderId)
                .orElseThrow(() -> new PaymentNotFoundException("주문을 찾을 수 없습니다."));
        // 주문 소유자 확인
        if (!order.getMember().getId().equals(memberId)) {
            throw new InvalidPaymentException("본인 주문이 아닙니다.");
        }

        if(order.getOrderStatus().getOrderStatusId() != orderStatusRepository.findByStatusName("결제대기").get().getOrderStatusId()) {
            throw new InvalidPaymentException("결제 대기중인 상품이 아닙니다.");
        }

        // 최종 결제금액
        int usedPoint = paymentRequest.getUsedPoint();
        int onlyBookAmount = order.getTotalPrice() - usedPoint;
        int finalPayAmount = order.getTotalPrice() + order.getDeliveryPrice() + order.getPackagingPrice() - usedPoint  ;
        if (finalPayAmount < 0) {
            throw new InvalidPaymentException("포인트 사용액이 주문 금액을 초과합니다.");
        }

        if (onlyBookAmount < 0 ) {
            onlyBookAmount = 0;
        }

        // Payment 생성
        Payment newPayment = new Payment();
        newPayment.setOrder(order);
        newPayment.setRequestedAt(LocalDateTime.now());
        newPayment.setPoint(usedPoint);
        newPayment.setTotalAmount(finalPayAmount);
        newPayment.setOnlyBookAmount(onlyBookAmount);
        newPayment.setCurrency((paymentRequest.getCurrency() != null) ? paymentRequest.getCurrency() : "KRW");

        if (finalPayAmount == 0) {
            // 전액 포인트 결제 → 즉시 DONE 처리
            newPayment.setStatus("DONE");
            orderService.updateOrderStatus(List.of(realOrderId), "배송전");
            commonPaymentService.handleFullPointPayment(newPayment, memberId);
        } else {
            // 토스/외부결제 진행을 위해 READY
            newPayment.setStatus("READY");
        }

        Payment saved = paymentRepository.save(newPayment);
        return buildPaymentResponse(saved, orderIdStr);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("결제 정보를 찾을 수 없습니다."));

        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrder().getOrderId().toString())
                .paymentKey(payment.getPaymentMethod() == null ? null : payment.getPaymentMethod().getPaymentKey())
                .totalAmount(payment.getTotalAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .requestedAt(payment.getRequestedAt())
                .approvedAt(payment.getApprovedAt())
                .usePoint(payment.getPoint())
                .onlyBookAmount(payment.getOnlyBookAmount())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CheckoutInfoResponse getCheckoutInfo(String orderIdStr, Long memberId) {
        Long realOrderId = parseOrderId(orderIdStr);
        Order order = orderRepository.findById(realOrderId)
                .orElseThrow(() -> new PaymentNotFoundException("주문을 찾을 수 없습니다."));

        if (!order.getMember().getId().equals(memberId)) {
            throw new PaymentNotFoundException("본인 주문이 아닙니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new PaymentNotFoundException("회원이 존재하지 않습니다."));
        Point point = pointRepository.findByMember_Id(memberId)
                .orElseThrow(() -> new PaymentNotFoundException("포인트 정보가 없습니다."));

        CheckoutInfoResponse dto = new CheckoutInfoResponse();
        dto.setOrderId(orderIdStr);
        dto.setOrderAmount(order.getTotalPrice() + order.getDeliveryPrice() + order.getPackagingPrice());
        dto.setUserPoint(point.getAmount());
        dto.setOrderName(order.getOrderName());

        dto.setOrdererName(member.getName());
        dto.setOrdererEmail(member.getEmail());
        dto.setOrdererPhone(member.getPhoneNumber());
        dto.setMemberId(memberId);

        return dto;
    }


    private Long parseOrderId(String orderIdStr) {
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

    private PaymentResponse buildPaymentResponse(Payment payment, String orderIdStr) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .orderId(orderIdStr)
                .paymentKey(
                        payment.getPaymentMethod() != null
                                ? payment.getPaymentMethod().getPaymentKey()
                                : null
                )
                .totalAmount(payment.getTotalAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .requestedAt(payment.getRequestedAt())
                .approvedAt(payment.getApprovedAt())
                .usePoint(payment.getPoint())
                .onlyBookAmount(payment.getOnlyBookAmount())
                .build();
    }
}

