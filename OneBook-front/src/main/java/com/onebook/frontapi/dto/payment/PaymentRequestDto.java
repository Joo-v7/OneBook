package com.onebook.frontapi.dto.payment;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDto {
    private int usedPoint;
    private String method;
    private int totalAmount;
    private String currency;
}