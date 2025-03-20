package com.nhnacademy.taskapi.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * [결제 응답] : Payment 정보 반환
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private long paymentId;
    private String orderId;
    private String paymentKey;    // tossPaymentKey ...
    private int totalAmount;
    private String currency;
    private String status;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private int usePoint;
    private int onlyBookAmount;
}
