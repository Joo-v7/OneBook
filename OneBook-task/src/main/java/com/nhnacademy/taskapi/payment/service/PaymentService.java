package com.nhnacademy.taskapi.payment.service;

import com.nhnacademy.taskapi.payment.dto.CheckoutInfoResponse;
import com.nhnacademy.taskapi.payment.dto.PaymentRequest;
import com.nhnacademy.taskapi.payment.dto.PaymentResponse;

/**
 * 결제 생성 (READY 상태), 결제 조회 등 기본 로직
 */
public interface PaymentService {
    PaymentResponse createPayment(String orderIdStr, Long memberId, PaymentRequest paymentRequest);
    PaymentResponse getPayment(Long paymentId);
    CheckoutInfoResponse getCheckoutInfo(String orderIdStr, Long memberId);
}
