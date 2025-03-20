package com.onebook.frontapi.dto.order;

import lombok.Getter;

@Getter
public class OrderDetailBookFeignResponse {
    private Long orderDetailId;
    private Long bookId;
    private String bookTitle;
    private int quantity;
    private int price;
    private int discountAmount;
}
