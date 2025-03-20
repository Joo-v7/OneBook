package com.onebook.frontapi.dto.coupon.response.coupon;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class IssuedCouponResponse {

    private Long issuedCouponId;
    private String couponNumber;
    private Long memberId;
    private LocalDateTime issueDateTime;
    private LocalDateTime useDateTime;

    // 쿠폰의 타입
    private String couponType;

    // 할인관련 정보
    private Integer minimumOrderAmount;
    private Integer discountRate;
    private Integer discountPrice;
    private Integer maximumDiscountPrice;

    // 유효기간 정보
    private LocalDateTime expirationPeriodStart;
    private LocalDateTime expirationPeriodEnd;

    // 쿠폰정책의 기본정보
    private String name;
    private String description;

    // 쿠폰의 적용대상에 대한 정보
    private String bookName;
    private String bookIsbn13;
    private String categoryName;

}
