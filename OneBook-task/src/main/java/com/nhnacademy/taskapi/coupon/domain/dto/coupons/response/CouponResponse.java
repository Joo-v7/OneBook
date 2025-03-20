package com.nhnacademy.taskapi.coupon.domain.dto.coupons.response;

import com.nhnacademy.taskapi.coupon.domain.entity.coupons.Coupon;
import com.nhnacademy.taskapi.coupon.exception.CouponHasNoPolicyExceptioin;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@Builder
public class CouponResponse {

    private String couponNumber;
    private String creationTime;
    private String couponStatus;

    private Long ratePolicyForBookId;
    private Long ratePolicyForCategoryId;
    private Long pricePolicyForBookId;
    private Long pricePolicyForCategoryId;

    public static CouponResponse changeEntityToDto(Coupon coupon){

        if(Objects.nonNull(coupon.getPricePolicyForBook())){
            return CouponResponse.builder()
                    .couponNumber(coupon.getCouponNumber())
                    .creationTime(coupon.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .couponStatus(coupon.getCouponStatus().getName())
                    .pricePolicyForBookId(coupon.getPricePolicyForBook().getPricePolicyForBookId())
                    .build();
        }

        if(Objects.nonNull(coupon.getPricePolicyForCategory())){
            return CouponResponse.builder()
                    .couponNumber(coupon.getCouponNumber())
                    .creationTime(coupon.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .couponStatus(coupon.getCouponStatus().getName())
                    .pricePolicyForCategoryId(coupon.getPricePolicyForCategory().getPricePolicyForCategoryId())
                    .build();
        }

        if(Objects.nonNull(coupon.getRatePolicyForBook())){
            return CouponResponse.builder()
                    .couponNumber(coupon.getCouponNumber())
                    .creationTime(coupon.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .couponStatus(coupon.getCouponStatus().getName())
                    .ratePolicyForBookId(coupon.getRatePolicyForBook().getRatePolicyForBookId())
                    .build();
        }

        if(Objects.nonNull(coupon.getRatePolicyForCategory())){
            return CouponResponse.builder()
                    .couponNumber(coupon.getCouponNumber())
                    .creationTime(coupon.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .couponStatus(coupon.getCouponStatus().getName())
                    .ratePolicyForCategoryId(coupon.getRatePolicyForCategory().getRatePolicyForCategoryId())
                    .build();

        }

        throw new CouponHasNoPolicyExceptioin("쿠폰에 지정된 정책이 존재하지 않습니다");
    }
}
