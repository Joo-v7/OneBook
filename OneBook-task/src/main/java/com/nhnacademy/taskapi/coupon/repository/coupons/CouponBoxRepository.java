package com.nhnacademy.taskapi.coupon.repository.coupons;

import com.nhnacademy.taskapi.coupon.domain.entity.coupons.Coupon;
import com.nhnacademy.taskapi.coupon.domain.entity.coupons.IssuedCoupon;
import com.nhnacademy.taskapi.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponBoxRepository extends JpaRepository<IssuedCoupon, Long> {

    boolean existsIssuedCouponByCouponAndMember(Coupon coupon, Member member);

    IssuedCoupon findByCoupon_CouponNumber(String couponNumber);
}
