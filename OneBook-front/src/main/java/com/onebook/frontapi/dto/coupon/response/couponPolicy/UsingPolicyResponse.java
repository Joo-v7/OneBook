package com.onebook.frontapi.dto.coupon.response.couponPolicy;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UsingPolicyResponse {

    private Long id;
    private Integer minimumOrderAmount;
    private Integer discountRate;
    private Integer maximumDiscountPrice;
    private Integer discountPrice;
    private LocalDateTime expirationPeriodStart;
    private LocalDateTime expirationPeriodEnd;
    private String name;
    private String description;
    private String bookName;
    private String bookIsbn13;
    private String categoryName;
    private String policyStatusName;
    private String type;
    private Long couponCount;
}
