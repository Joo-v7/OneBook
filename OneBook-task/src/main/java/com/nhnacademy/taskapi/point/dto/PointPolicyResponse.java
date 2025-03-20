package com.nhnacademy.taskapi.point.dto;

import com.nhnacademy.taskapi.point.domain.PointPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointPolicyResponse {
    private Long pointPolicyId;
    private String pointPolicyName;
    private double pointPolicyConditionAmount;
    private double pointPolicyRate;
    private double pointPolicyApplyAmount;
    private String pointPolicyCondition;
    private Boolean pointPolicyApplyType;
    private LocalDateTime pointPolicyCreatedAt;
    private LocalDateTime pointPolicyUpdatedAt;
    private Boolean pointPolicyState;

    // PointPolicy 엔티티를 받아서 초기화하는 생성자
    public PointPolicyResponse(PointPolicy pointPolicy) {
        this.pointPolicyId = pointPolicy.getPointPolicyId();
        this.pointPolicyName = pointPolicy.getPointPolicyName();
        this.pointPolicyConditionAmount = pointPolicy.getPointPolicyConditionAmount();
        this.pointPolicyRate = pointPolicy.getPointPolicyRate();
        this.pointPolicyApplyAmount = pointPolicy.getPointPolicyApplyAmount();
        this.pointPolicyCondition = pointPolicy.getPointPolicyCondition();
        this.pointPolicyApplyType = pointPolicy.isPointPolicyApplyType(); // 기존 코드와 동일
        this.pointPolicyCreatedAt = pointPolicy.getPointPolicyCreatedAt();
        this.pointPolicyUpdatedAt = pointPolicy.getPointPolicyUpdatedAt();
        this.pointPolicyState = pointPolicy.isPointPolicyState();
    }

    public boolean isPointPolicyApplyType() {
        return pointPolicyApplyType;
    }
}
