package com.onebook.frontapi.dto.coupon.response.couponPolicy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PricePolicyForCategoryAndCouponCountResponse {
    private Long id;
    private Integer minimumOrderAmount;
    private Integer discountPrice;
    private LocalDateTime expirationPeriodStart;
    private LocalDateTime expirationPeriodEnd;
    private String name;
    private String description;
    private String categoryName;
    private String policyStatusName;
    private Long couponCount;

    public static PricePolicyForCategoryAndCouponCountResponse changeFeignResponseToResponse(PricePolicyForCategoryResponse pricePolicyForCategoryResponse, Long count){

        return new PricePolicyForCategoryAndCouponCountResponse(
                pricePolicyForCategoryResponse.getId(),
                pricePolicyForCategoryResponse.getMinimumOrderAmount(),
                pricePolicyForCategoryResponse.getDiscountPrice(),
                pricePolicyForCategoryResponse.getExpirationPeriodStart(),
                pricePolicyForCategoryResponse.getExpirationPeriodEnd(),
                pricePolicyForCategoryResponse.getName(),
                pricePolicyForCategoryResponse.getDescription(),
                pricePolicyForCategoryResponse.getCategoryName(),
                pricePolicyForCategoryResponse.getPolicyStatusName(),
                count
        );
    }
}
