package com.onebook.frontapi.dto.payment.toss;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TossConfirmResponseDto {
    private String paymentKey;
    private String orderId;
    private String status;
    private String approvedAt;
    private String message;

    private String memberId;
}