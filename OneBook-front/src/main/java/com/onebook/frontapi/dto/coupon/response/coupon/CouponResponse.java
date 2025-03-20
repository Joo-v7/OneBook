package com.onebook.frontapi.dto.coupon.response.coupon;

import lombok.Getter;

@Getter
public class CouponResponse {
    private String couponNumber;
    private String creationTime;
    private String couponStatus;

    private Long ratePolicyForBookId;
    private Long ratePolicyForCategoryId;
    private Long pricePolicyForBookId;
    private Long pricePolicyForCategoryId;
}
