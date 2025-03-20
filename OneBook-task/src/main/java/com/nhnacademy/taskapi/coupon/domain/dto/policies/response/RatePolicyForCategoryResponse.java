package com.nhnacademy.taskapi.coupon.domain.dto.policies.response;

import com.nhnacademy.taskapi.coupon.domain.entity.policies.RatePolicyForCategory;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class RatePolicyForCategoryResponse {

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

    public static RatePolicyForCategoryResponse changeEntityToDto(RatePolicyForCategory ratePolicyForCategory){
        return new RatePolicyForCategoryResponse(
                ratePolicyForCategory.getRatePolicyForCategoryId(),
                ratePolicyForCategory.getDiscountRate(),
                ratePolicyForCategory.getMinimumOrderAmount(),
                ratePolicyForCategory.getMaximumDiscountPrice(),
                ratePolicyForCategory.getExpirationPeriodStart(),
                ratePolicyForCategory.getExpirationPeriodEnd(),
                ratePolicyForCategory.getName(),
                ratePolicyForCategory.getDescription(),
                ratePolicyForCategory.getCategory().getName(),
                ratePolicyForCategory.getPolicyStatus().getName()
        );
    }
}
