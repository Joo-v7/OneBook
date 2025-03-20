package com.nhnacademy.taskapi.point.service;

import com.nhnacademy.taskapi.point.domain.PointPolicy;
import com.nhnacademy.taskapi.point.dto.CreatePointPolicyRequest;
import com.nhnacademy.taskapi.point.dto.PointPolicyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PointPolicyService {

    // 포인트 정책 생성
    PointPolicyResponse createPointPolicy(CreatePointPolicyRequest createPointRequest);

    // 모든 포인트 정책 조회 (페이지네이션)
    Page<PointPolicyResponse> getAllPointPolicies(Pageable pageable);

    // 특정 포인트 정책 조회
    PointPolicyResponse getPointPolicyById(Long pointId);

    // 포인트 정책 비활성화
    void deactivatePointPolicy(Long pointId);

    // 활성화된 포인트 정책 조회 (전체 리스트)
    List<PointPolicyResponse> getActivePointPolicies();

    // 포인트 정책 업데이트
    void updatePointPolicyById(Long pointPolicyId, PointPolicy pointPolicy);
}
