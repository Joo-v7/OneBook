package com.nhnacademy.taskapi.payment.dto.toss;

import lombok.Data;

/**
 * [Front -> TaskAPI] : 토스 결제승인 요청
 */
@Data
public class TossConfirmRequest {
    private String paymentKey;   // Toss에서 전달되는 paymentKey
    private String orderId;      // 상점 측 orderId
    private String amount;       // 결제 승인할 금액(문자열)
}