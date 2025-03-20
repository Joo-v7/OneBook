package com.nhnacademy.taskapi.coupon.domain.dto.policies.response;

import com.nhnacademy.taskapi.coupon.domain.entity.policies.*;
import com.nhnacademy.taskapi.coupon.exception.PolicyNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class UsingPolicyResponse {

    private Long id;
    private Integer minimumOrderAmount;
    private Integer discountRate;
    private Integer maximumDiscountPrice;
    private Integer discountPrice;
    private LocalDateTime expirationPeriodStart;
    private LocalDateTime expirationPeriodEnd;
    private String name;
    private String description;
    private String bookName;
    private String bookIsbn13;
    private String categoryName;
    private String policyStatusName;
    private String type;
    private Long couponCount;

    public static UsingPolicyResponse changePolicyToPolicyResponse(Policy policy, Long couponCount){

        if(policy instanceof RatePolicyForBook){

            return UsingPolicyResponse.builder().
                    id(((RatePolicyForBook) policy).getRatePolicyForBookId()).
                    minimumOrderAmount(((RatePolicyForBook) policy).getMinimumOrderAmount()).
                    discountRate(((RatePolicyForBook) policy).getDiscountRate()).
                    maximumDiscountPrice(((RatePolicyForBook) policy).getMaximumDiscountPrice()).
                    expirationPeriodStart(((RatePolicyForBook) policy).getExpirationPeriodStart()).
                    expirationPeriodEnd(((RatePolicyForBook) policy).getExpirationPeriodEnd()).
                    name(((RatePolicyForBook) policy).getName()).
                    description(((RatePolicyForBook) policy).getDescription()).
                    bookName(((RatePolicyForBook) policy).getBook().getTitle()).
                    bookIsbn13(((RatePolicyForBook) policy).getBook().getIsbn13()).
                    policyStatusName(((RatePolicyForBook) policy).getPolicyStatus().getName()).
                    type("정률정책/책").
                    couponCount(couponCount).build();
        }

        if(policy instanceof RatePolicyForCategory){

            return UsingPolicyResponse.builder().
                    id(((RatePolicyForCategory) policy).getRatePolicyForCategoryId()).
                    minimumOrderAmount(((RatePolicyForCategory) policy).getMinimumOrderAmount()).
                    discountRate(((RatePolicyForCategory) policy).getDiscountRate()).
                    maximumDiscountPrice(((RatePolicyForCategory) policy).getMaximumDiscountPrice()).
                    expirationPeriodStart(((RatePolicyForCategory) policy).getExpirationPeriodStart()).
                    expirationPeriodEnd(((RatePolicyForCategory) policy).getExpirationPeriodEnd()).
                    name(((RatePolicyForCategory) policy).getName()).
                    description(((RatePolicyForCategory) policy).getDescription()).
                    categoryName(((RatePolicyForCategory) policy).getCategory().getName()).
                    policyStatusName(((RatePolicyForCategory) policy).getPolicyStatus().getName()).
                    type("정률정책/카테고리").
                    couponCount(couponCount).build();
        }

        if(policy instanceof PricePolicyForBook){

            return UsingPolicyResponse.builder().
                    id(((PricePolicyForBook) policy).getPricePolicyForBookId()).
                    minimumOrderAmount(((PricePolicyForBook) policy).getMinimumOrderAmount()).
                    discountPrice(((PricePolicyForBook) policy).getDiscountPrice()).
                    expirationPeriodStart(((PricePolicyForBook) policy).getExpirationPeriodStart()).
                    expirationPeriodEnd(((PricePolicyForBook) policy).getExpirationPeriodEnd()).
                    name(((PricePolicyForBook) policy).getName()).
                    description(((PricePolicyForBook) policy).getDescription()).
                    bookName(((PricePolicyForBook) policy).getBook().getTitle()).
                    bookIsbn13(((PricePolicyForBook) policy).getBook().getIsbn13()).
                    policyStatusName(((PricePolicyForBook) policy).getPolicyStatus().getName()).
                    type("정액정책/책").
                    couponCount(couponCount).build();


        }

        if(policy instanceof PricePolicyForCategory){

            return UsingPolicyResponse.builder().
                    id(((PricePolicyForCategory) policy).getPricePolicyForCategoryId()).
                    minimumOrderAmount(((PricePolicyForCategory) policy).getMinimumOrderAmount()).
                    discountPrice(((PricePolicyForCategory) policy).getDiscountPrice()).
                    expirationPeriodStart(((PricePolicyForCategory) policy).getExpirationPeriodStart()).
                    expirationPeriodEnd(((PricePolicyForCategory) policy).getExpirationPeriodEnd()).
                    name(((PricePolicyForCategory) policy).getName()).
                    description(((PricePolicyForCategory) policy).getDescription()).
                    categoryName(((PricePolicyForCategory) policy).getCategory().getName()).
                    policyStatusName(((PricePolicyForCategory) policy).getPolicyStatus().getName()).
                    type("정액정책/카테고리").
                    couponCount(couponCount).build();
        }

        throw new PolicyNotFoundException("해당하는 타입의 정책을 찾을 수 없습니다");
    }
}
