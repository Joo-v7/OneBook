package com.onebook.frontapi.dto.payment;

import lombok.Data;

@Data
public class CheckoutInfoResponseDto {
    private String orderId;
    private int orderAmount;
    private int userPoint;
    private String orderName;

    // 주문자 정보
    private String ordererName;
    private String ordererEmail;
    private String ordererPhone;

    private String memberId;
}
