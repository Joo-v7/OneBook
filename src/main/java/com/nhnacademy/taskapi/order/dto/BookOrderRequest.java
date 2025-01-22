package com.nhnacademy.taskapi.order.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BookOrderRequest {
    long orderDetailId;
    long bookId;
    int quantity;
    int discountAmount;
    int price;
    int discountedPrice;
    String couponNumber;
}
