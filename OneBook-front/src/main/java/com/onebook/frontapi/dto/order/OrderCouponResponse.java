package com.onebook.frontapi.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class OrderCouponResponse {
    private String couponNumber;
    private String couponName;
    private int discountPrice;
    private LocalDate expirationDate;
}
