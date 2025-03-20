package com.onebook.frontapi.dto.book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookOrderRequest {
    private static final long serialVersionUID = 1L;

    long orderDetailId;
    long bookId;
    int quantity;
    int discountAmount;
    int price;
    int discountedPrice;
    String couponNumber;
}
