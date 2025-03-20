package com.nhnacademy.taskapi.point.jpa;

import com.nhnacademy.taskapi.point.domain.PointPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaPointPolicyRepository extends JpaRepository<PointPolicy, Long> {
    PointPolicy findByPointPolicyName(String pointPolicyName);

    // 페이지네이션 없이 모든 포인트 정책을 조회하는 메서드
    Page<PointPolicy> findAllBy(Pageable pageable);

    // 페이지네이션을 고려한 활성화된 정책만 조회하는 메서드 (pointPolicyState가 true인 경우)
    Page<PointPolicy> findAllByPointPolicyStateTrue(Pageable pageable);

    // 모든 활성화된 포인트 정책을 조회하는 메서드
    List<PointPolicy> findAllByPointPolicyStateTrue();

    // 정렬 기준을 추가하는 메서드
    Page<PointPolicy> findAllByOrderByPointPolicyCreatedAtAscPointPolicyStateDesc(Pageable pageable);
}
