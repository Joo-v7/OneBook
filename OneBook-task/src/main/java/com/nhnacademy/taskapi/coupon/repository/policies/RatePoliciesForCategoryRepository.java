package com.nhnacademy.taskapi.coupon.repository.policies;

import com.nhnacademy.taskapi.coupon.domain.entity.policies.RatePolicyForCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatePoliciesForCategoryRepository extends JpaRepository<RatePolicyForCategory, Long> {
    Page<RatePolicyForCategory> findByPolicyStatus_NameOrPolicyStatus_Name(String policyStatusName, String policyStatusName1, Pageable pageable);
}
