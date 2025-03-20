package com.onebook.frontapi.dto.coupon.response.couponPolicy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PricePolicyForBookAndCouponCountResponse {
    private Long id;
    private Integer minimumOrderAmount;
    private Integer discountPrice;
    private LocalDateTime expirationPeriodStart;
    private LocalDateTime expirationPeriodEnd;
    private String name;
    private String description;
    private String bookName;
    private String bookIsbn13;
    private String policyStatusName;
    private Long couponCount;

    public static PricePolicyForBookAndCouponCountResponse changeFeignResponseToResponse(PricePolicyForBookResponse pricePolicyForBookResponse, Long count){

        return new PricePolicyForBookAndCouponCountResponse(
                pricePolicyForBookResponse.getId(),
                pricePolicyForBookResponse.getMinimumOrderAmount(),
                pricePolicyForBookResponse.getDiscountPrice(),
                pricePolicyForBookResponse.getExpirationPeriodStart(),
                pricePolicyForBookResponse.getExpirationPeriodEnd(),
                pricePolicyForBookResponse.getName(),
                pricePolicyForBookResponse.getDescription(),
                pricePolicyForBookResponse.getBookName(),
                pricePolicyForBookResponse.getBookIsbn13(),
                pricePolicyForBookResponse.getPolicyStatusName(),
                count
        );
    }
}
