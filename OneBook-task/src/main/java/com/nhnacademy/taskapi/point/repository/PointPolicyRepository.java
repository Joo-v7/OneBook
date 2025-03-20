package com.nhnacademy.taskapi.point.repository;

import com.nhnacademy.taskapi.point.domain.PointPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointPolicyRepository extends JpaRepository<PointPolicy, Long> {

    @Query("SELECT pp FROM PointPolicy pp WHERE pp.pointPolicyState = true ORDER BY pp.pointPolicyCreatedAt DESC")
    Page<PointPolicy> findByPointPolicyStateTrue(Pageable pageable);
    List<PointPolicy> findByPointPolicyState(boolean pointPolicyState);
}



//활성화된 포인트 정책만 조회할 수 있도록