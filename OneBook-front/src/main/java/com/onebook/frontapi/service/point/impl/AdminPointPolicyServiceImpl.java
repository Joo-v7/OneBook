package com.onebook.frontapi.service.point.impl;

import com.onebook.frontapi.dto.point.request.CreatePointPolicyRequest;
import com.onebook.frontapi.dto.point.request.PointPolicyRequest;
import com.onebook.frontapi.dto.point.request.UpdatePointPolicyRequest;
import com.onebook.frontapi.dto.point.response.PointPolicyResponse;
import com.onebook.frontapi.feign.point.PointPolicyFeignClient;
import com.onebook.frontapi.service.point.AdminPointPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminPointPolicyServiceImpl implements AdminPointPolicyService {

    @Autowired
    private PointPolicyFeignClient pointPolicyFeignClient;

    // 활성화된 포인트 정책 목록 조회
    @Override
    public Page<PointPolicyResponse> getActivePointPolicies(Pageable pageable) {
        // pageable에서 page와 size 값을 추출
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        // 활성화된 포인트 정책 목록 조회
        Page<PointPolicyResponse> response = pointPolicyFeignClient.getAllPointPolicies(page, size);

        if (response != null) {
            return new PageImpl<>(response.getContent(), pageable, response.getTotalElements());
        }
        return Page.empty();
    }

    // 특정 포인트 정책 조회
    @Override
    public PointPolicyResponse getPointPolicyById(Long pointPolicyId) {
        return pointPolicyFeignClient.getPointPolicyById(pointPolicyId);
    }

    // 포인트 정책 생성
    @Override
    public void createPointPolicy(PointPolicyRequest request) {
        CreatePointPolicyRequest createRequest = CreatePointPolicyRequest.builder()
                .pointPolicyName(request.getPointPolicyName())
                .pointPolicyRate(request.getPointPolicyRate())
                .pointPolicyConditionAmount(request.getPointPolicyConditionAmount())
                .pointPolicyApplyAmount(request.getPointPolicyApplyAmount())
                .pointPolicyCondition(request.getPointPolicyCondition())
                .pointPolicyApplyType(request.isPointPolicyApplyType())
                .build();

        pointPolicyFeignClient.createPointPolicy(createRequest);
    }

    // 포인트 정책 수정
    @Override
    public void updatePointPolicy(Long pointPolicyId, PointPolicyRequest request) {
        UpdatePointPolicyRequest updateRequest = new UpdatePointPolicyRequest();
        updateRequest.setPointPolicyName(request.getPointPolicyName());
        updateRequest.setPointPolicyConditionAmount(request.getPointPolicyConditionAmount());
        updateRequest.setPointPolicyApplyAmount(request.getPointPolicyApplyAmount());
        updateRequest.setPointPolicyRate(request.getPointPolicyRate().intValue());
        updateRequest.setPointPolicyCondition(request.getPointPolicyCondition());
        updateRequest.setPointPolicyApplyType(request.isPointPolicyApplyType());
        updateRequest.setPointPolicyState(true);

        pointPolicyFeignClient.updatePointPolicy(pointPolicyId, updateRequest);
    }

    // 포인트 정책 비활성화 (삭제)
    @Override
    public void deactivatePointPolicy(Long pointPolicyId) {
        pointPolicyFeignClient.deactivatePointPolicy(pointPolicyId);
    }
}
