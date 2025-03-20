package com.nhnacademy.taskapi.coupon.repository.policies;

import com.nhnacademy.taskapi.coupon.domain.entity.policies.PricePolicyForBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricePoliciesForBookRepository extends JpaRepository<PricePolicyForBook, Long> {
    Page<PricePolicyForBook> findByPolicyStatus_NameOrPolicyStatus_Name(String policyStatusName, String policyStatusName1, Pageable pageable);
}
