package com.onebook.frontapi.dto.coupon.response.couponPolicy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class RatePolicyForCategoryAndCouponCountResponse {
    private Long id;
    private Integer discountRate;
    private Integer minimumOrderAmount;
    private Integer maximumDiscountPrice;
    private LocalDateTime expirationPeriodStart;
    private LocalDateTime expirationPeriodEnd;
    private String name;
    private String description;
    private String categoryName;
    private String policyStatusName;
    private Long couponCount;

    public static RatePolicyForCategoryAndCouponCountResponse changeFeignResponseToResponse(RatePolicyForCategoryResponse ratePolicyForCategoryResponse, Long count){

        return new RatePolicyForCategoryAndCouponCountResponse(
                ratePolicyForCategoryResponse.getId(),
                ratePolicyForCategoryResponse.getDiscountRate(),
                ratePolicyForCategoryResponse.getMinimumOrderAmount(),
                ratePolicyForCategoryResponse.getMaximumDiscountPrice(),
                ratePolicyForCategoryResponse.getExpirationPeriodStart(),
                ratePolicyForCategoryResponse.getExpirationPeriodEnd(),
                ratePolicyForCategoryResponse.getName(),
                ratePolicyForCategoryResponse.getDescription(),
                ratePolicyForCategoryResponse.getCategoryName(),
                ratePolicyForCategoryResponse.getPolicyStatusName(),
                count
        );
    }
}
