package com.nhnacademy.taskapi.point.dto;

import com.nhnacademy.taskapi.point.domain.PointPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePointPolicyRequest {
    private Long pointPolicyId;  // Long 타입
    private String pointPolicyName;
    private Integer pointPolicyConditionAmount;  // Integer 타입
    private Integer pointPolicyRate;  // Integer 타입
    private Integer pointPolicyApplyAmount;  // Integer 타입
    private String pointPolicyCondition;
    private boolean pointPolicyApplyType;
    private boolean pointPolicyState;

    // 엔티티 변환 메서드
    public PointPolicy toEntity() {
        return PointPolicy.builder()
                .pointPolicyId(this.pointPolicyId)
                .pointPolicyName(this.pointPolicyName)
                .pointPolicyConditionAmount(this.pointPolicyConditionAmount)
                .pointPolicyRate(this.pointPolicyRate)
                .pointPolicyApplyAmount(this.pointPolicyApplyAmount)
                .pointPolicyCondition(this.pointPolicyCondition)
                .pointPolicyApplyType(this.pointPolicyApplyType)
                .pointPolicyState(this.pointPolicyState)
                .build();
    }
}
