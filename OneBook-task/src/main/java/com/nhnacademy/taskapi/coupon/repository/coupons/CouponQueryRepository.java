package com.nhnacademy.taskapi.coupon.repository.coupons;

import com.nhnacademy.taskapi.coupon.domain.entity.coupons.QCoupon;
import com.nhnacademy.taskapi.coupon.domain.entity.status.CouponStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QCoupon coupon = QCoupon.coupon;

    public Long getValidCouponCountByRatePolicyForBook(Long ratePolicyForBookId, CouponStatus couponStatus){

        Long count =
                jpaQueryFactory
                        .selectFrom(coupon)
                        .where(
                                coupon.ratePolicyForBook.ratePolicyForBookId.eq(ratePolicyForBookId),
                                coupon.couponStatus.eq(couponStatus)
                        ).stream().count();

        return count;
    }

    public Long getValidCouponCountByRatePolicyForCategory(Long ratePolicyForCategoryId, CouponStatus couponStatus){
        Long count =
                jpaQueryFactory
                        .selectFrom(coupon)
                        .where(
                                coupon.ratePolicyForCategory.ratePolicyForCategoryId.eq(ratePolicyForCategoryId),
                                coupon.couponStatus.eq(couponStatus)
                        ).stream().count();

        return count;
    }

    public Long getValidCouponCountByPricePolicyForBook(Long pricePolicyForBookId, CouponStatus couponStatus){
        Long count =
                jpaQueryFactory
                        .selectFrom(coupon)
                        .where(
                                coupon.pricePolicyForBook.pricePolicyForBookId.eq(pricePolicyForBookId),
                                coupon.couponStatus.eq(couponStatus)
                        ).stream().count();

        return count;


    }

    public Long getValidCouponCountByPricePolicyForCategory(Long pricePolicyForCategoryId, CouponStatus couponStatus){
        Long count =
                jpaQueryFactory
                        .selectFrom(coupon)
                        .where(
                                coupon.pricePolicyForCategory.pricePolicyForCategoryId.eq(pricePolicyForCategoryId),
                                coupon.couponStatus.eq(couponStatus)
                        ).stream().count();

        return count;
    }
}
