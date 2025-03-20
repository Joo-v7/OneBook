package com.onebook.frontapi.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {
    private long paymentId;
    private String orderId;
    private String paymentKey;
    private int totalAmount;
    private String currency;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private int usePoint;
}
