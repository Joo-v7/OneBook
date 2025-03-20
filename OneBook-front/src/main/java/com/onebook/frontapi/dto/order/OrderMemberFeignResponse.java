package com.onebook.frontapi.dto.order;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderMemberFeignResponse {
    Long orderId;
    Long memberId;
    String orderName;
    String ordererName;
    String ordererPhoneNumber;
    LocalDateTime dateTime;
    int deliveryPrice;
    int totalPrice;
    String statusName;
    int packagingPrice;
}
