package com.onebook.frontapi.dto.coupon.request.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindCouponsByPolicyIdRequest {

    private Long policyId;
    private String couponType;
}
