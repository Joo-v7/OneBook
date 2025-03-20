package com.nhnacademy.taskapi.coupon.repository.status;

import com.nhnacademy.taskapi.coupon.domain.entity.status.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponStatusRepository extends JpaRepository<CouponStatus ,Integer> {
    CouponStatus findByName(String name);
}
