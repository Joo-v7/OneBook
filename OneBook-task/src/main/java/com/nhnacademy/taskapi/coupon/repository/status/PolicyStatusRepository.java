package com.nhnacademy.taskapi.coupon.repository.status;

import com.nhnacademy.taskapi.coupon.domain.entity.status.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyStatusRepository extends JpaRepository<PolicyStatus, Integer> {
    PolicyStatus findByName(String name);
}
