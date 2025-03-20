package com.nhnacademy.taskapi.coupon.domain.dto.coupons.response;

import com.nhnacademy.taskapi.coupon.domain.entity.coupons.Coupon;
import com.nhnacademy.taskapi.coupon.domain.entity.coupons.IssuedCoupon;
import com.nhnacademy.taskapi.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class IssuedCouponResponse {

    private Long issuedCouponId;
    private String couponNumber;
    private Long memberId;
    private LocalDateTime issueDateTime;
    private LocalDateTime useDateTime;

    public static IssuedCouponResponse changeEntityToDto(IssuedCoupon issuedCoupon){

        return new IssuedCouponResponse(
                issuedCoupon.getIssuedCouponId(),
                issuedCoupon.getCoupon().getCouponNumber(),
                issuedCoupon.getMember().getId(),
                issuedCoupon.getIssueDateTime(),
                issuedCoupon.getUseDateTime()
        );
    }
}
