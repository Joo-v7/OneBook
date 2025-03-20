package com.onebook.frontapi.dto.coupon.response.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class IssuedCouponFeignResponse {

    private Long issuedCouponId;
    private String couponNumber;
    private Long memberId;
    private LocalDateTime issueDateTime;
    private LocalDateTime useDateTime;
}
