package com.nhnacademy.taskapi.coupon.domain.dto.coupons.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class CreateCouponRequest {

    private Long policyId;
    private Integer count;
}
