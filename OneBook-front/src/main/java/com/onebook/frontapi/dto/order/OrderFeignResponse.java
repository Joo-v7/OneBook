package com.onebook.frontapi.dto.order;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderFeignResponse {
    private Long orderId;
    private Long memberId;
    private String orderName;
    private String ordererName;
    private String ordererPhoneNumber;
    private LocalDateTime dateTime;
    private int deliveryPrice;
    private int totalPrice;
    private String orderStatusName;
    private int packagingPrice;
}
