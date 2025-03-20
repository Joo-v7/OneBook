package com.nhnacademy.taskapi.coupon.repository.coupons;

import com.nhnacademy.taskapi.coupon.domain.entity.coupons.Coupon;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.PricePolicyForBook;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.PricePolicyForCategory;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.RatePolicyForBook;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.RatePolicyForCategory;
import com.nhnacademy.taskapi.coupon.domain.entity.status.CouponStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCouponNumber(String couponNumber);

    List<Coupon> findByRatePolicyForBookAndCouponStatus(RatePolicyForBook ratePolicyForBook, CouponStatus couponStatus);

    List<Coupon> findByRatePolicyForCategoryAndCouponStatus(RatePolicyForCategory ratePolicyForCategory, CouponStatus couponStatus);

    List<Coupon> findByPricePolicyForBookAndCouponStatus(PricePolicyForBook pricePolicyForBook, CouponStatus couponStatus);

    List<Coupon> findByPricePolicyForCategoryAndCouponStatus(PricePolicyForCategory pricePolicyForCategory, CouponStatus couponStatus);
}
