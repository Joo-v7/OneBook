package com.onebook.frontapi.dto.coupon.response.couponPolicy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class RatePolicyForBookAndCouponCountResponse {
    private Long id;
    private Integer discountRate;
    private Integer minimumOrderAmount;
    private Integer maximumDiscountPrice;
    private LocalDateTime expirationPeriodStart;
    private LocalDateTime expirationPeriodEnd;
    private String name;
    private String description;
    private String bookName;
    private String bookIsbn13;
    private String policyStatusName;
    private Long couponCount;

    public static RatePolicyForBookAndCouponCountResponse changeFeignResponseToResponse(RatePolicyForBookResponse ratePolicyForBookResponse, Long count){

        return new RatePolicyForBookAndCouponCountResponse(
                ratePolicyForBookResponse.getId(),
                ratePolicyForBookResponse.getDiscountRate(),
                ratePolicyForBookResponse.getMinimumOrderAmount(),
                ratePolicyForBookResponse.getMaximumDiscountPrice(),
                ratePolicyForBookResponse.getExpirationPeriodStart(),
                ratePolicyForBookResponse.getExpirationPeriodEnd(),
                ratePolicyForBookResponse.getName(),
                ratePolicyForBookResponse.getDescription(),
                ratePolicyForBookResponse.getBookName(),
                ratePolicyForBookResponse.getBookIsbn13(),
                ratePolicyForBookResponse.getPolicyStatusName(),
                count
        );
    }
}
