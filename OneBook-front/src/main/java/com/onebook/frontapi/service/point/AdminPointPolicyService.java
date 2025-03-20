package com.onebook.frontapi.service.point;

import com.onebook.frontapi.dto.point.request.PointPolicyRequest;
import com.onebook.frontapi.dto.point.response.PointPolicyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface AdminPointPolicyService {
    // 활성화된 포인트 정책 목록 조회
    Page<PointPolicyResponse> getActivePointPolicies(Pageable pageable);

    // 특정 포인트 정책 조회
    PointPolicyResponse getPointPolicyById(Long pointPolicyId);

    // 포인트 정책 생성
    void createPointPolicy(PointPolicyRequest request);

    // 포인트 정책 수정
    void updatePointPolicy(Long pointPolicyId, PointPolicyRequest request);

    // 포인트 정책 비활성화 (삭제)
    void deactivatePointPolicy(Long pointPolicyId);
}
