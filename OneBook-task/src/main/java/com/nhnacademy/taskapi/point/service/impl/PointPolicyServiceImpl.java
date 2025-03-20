package com.nhnacademy.taskapi.point.service.impl;

import com.nhnacademy.taskapi.point.domain.PointPolicy;
import com.nhnacademy.taskapi.point.dto.CreatePointPolicyRequest;
import com.nhnacademy.taskapi.point.dto.PointPolicyResponse;
import com.nhnacademy.taskapi.point.repository.PointPolicyRepository;
import com.nhnacademy.taskapi.point.service.PointPolicyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointPolicyServiceImpl implements PointPolicyService {

    private final PointPolicyRepository pointPolicyRepository;

    @Override
    public PointPolicyResponse createPointPolicy(CreatePointPolicyRequest createPointRequest) {
        PointPolicy pointPolicy = createPointRequest.toEntity();
        PointPolicy savedPolicy = pointPolicyRepository.save(pointPolicy);
        return new PointPolicyResponse(savedPolicy);
    }

    @Override
    public Page<PointPolicyResponse> getAllPointPolicies(Pageable pageable) {
        return pointPolicyRepository.findAll(pageable)
                .map(PointPolicyResponse::new);
    }

    @Override
    public PointPolicyResponse getPointPolicyById(Long pointId) {
        PointPolicy pointPolicy = pointPolicyRepository.findById(pointId)
                .orElseThrow(() -> new EntityNotFoundException("포인트 정책을 찾을 수 없습니다. ID: " + pointId));
        return new PointPolicyResponse(pointPolicy);
    }

    @Override
    public void deactivatePointPolicy(Long pointId) {
        PointPolicy pointPolicy = pointPolicyRepository.findById(pointId)
                .orElseThrow(() -> new EntityNotFoundException("포인트 정책을 찾을 수 없습니다. ID: " + pointId));
        pointPolicy.setPointPolicyState(false); // 비활성화 처리
        pointPolicyRepository.save(pointPolicy);
    }

    @Override
    public List<PointPolicyResponse> getActivePointPolicies() {
        List<PointPolicy> activePolicies = pointPolicyRepository.findByPointPolicyState(true);
        return activePolicies.stream()
                .map(PointPolicyResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public void updatePointPolicyById(Long pointPolicyId, PointPolicy updatedPolicy) {
        PointPolicy existingPolicy = pointPolicyRepository.findById(pointPolicyId)
                .orElseThrow(() -> new EntityNotFoundException("포인트 정책을 찾을 수 없습니다. ID: " + pointPolicyId));

        // 기존 정책 정보 업데이트
        existingPolicy.setPointPolicyName(updatedPolicy.getPointPolicyName());
        existingPolicy.setPointPolicyRate(updatedPolicy.getPointPolicyRate());
        existingPolicy.setPointPolicyConditionAmount(updatedPolicy.getPointPolicyConditionAmount());
        existingPolicy.setPointPolicyApplyAmount(updatedPolicy.getPointPolicyApplyAmount());
        existingPolicy.setPointPolicyCondition(updatedPolicy.getPointPolicyCondition());
        existingPolicy.setPointPolicyApplyType(updatedPolicy.isPointPolicyApplyType());
        existingPolicy.setPointPolicyState(updatedPolicy.isPointPolicyState());

        pointPolicyRepository.save(existingPolicy);
    }
}
