package com.onebook.frontapi.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class OrderRegisterResponseDto {
    String packagingName;
    int packagingPrice;
    String ordererName;
    String ordererPhone;
    String recipientName;
    String recipientPhone;
    String recipientAddress;
    String recipientRequestedTerm;
}