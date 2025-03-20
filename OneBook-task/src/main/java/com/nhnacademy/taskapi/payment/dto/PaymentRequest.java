package com.nhnacademy.taskapi.payment.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * [결제 요청] : 사용 포인트, 결제수단(method), 주문금액 등
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private int usedPoint;      // 사용 포인트
    private String method;       // Toss, NaverPay, ...
    private int totalAmount;     // 주문 총 금액
    private String currency;     // KRW 등
}