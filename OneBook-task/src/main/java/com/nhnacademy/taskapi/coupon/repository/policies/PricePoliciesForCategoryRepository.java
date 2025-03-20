package com.nhnacademy.taskapi.coupon.repository.policies;

import com.nhnacademy.taskapi.coupon.domain.entity.policies.PricePolicyForCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricePoliciesForCategoryRepository extends JpaRepository<PricePolicyForCategory, Long> {
    Page<PricePolicyForCategory> findByPolicyStatus_NameOrPolicyStatus_Name(String policyStatusName, String policyStatusName1, Pageable pageable);
}
