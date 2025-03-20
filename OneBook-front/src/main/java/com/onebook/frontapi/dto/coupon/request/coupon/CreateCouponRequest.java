package com.onebook.frontapi.dto.coupon.request.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateCouponRequest {
    private Long policyId;
    private Integer count;
}
