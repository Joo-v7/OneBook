package com.nhnacademy.taskapi.payment.dto.toss;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * [TaskAPI -> Front] : 토스 결제승인 응답
 */
@Data
public class TossConfirmResponse {
    private String paymentKey;   // 최종 확정된 paymentKey
    private String orderId;      // 주문 ID
    private String status;       // DONE, etc
    private LocalDateTime approvedAt;
    private String message;      // "결제 승인 성공" 등

    private Long memberId;
}
