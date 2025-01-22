package com.nhnacademy.taskapi.point.dto;

import com.nhnacademy.taskapi.point.domain.PointPolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointPolicyRequest {
    private Long pointPolicyId;
    private String pointPolicyName;
    private Integer pointPolicyConditionAmount;
    private Integer pointPolicyRate;
    private Integer pointPolicyApplyAmount;
    private String pointPolicyCondition;
    private boolean pointPolicyApplyType;
    private boolean pointPolicyState;

    public CreatePointPolicyRequest toCreatePointPolicyRequest() {
        return new CreatePointPolicyRequest(
                this.pointPolicyId,  // ID 추가
                this.pointPolicyName,
                this.getPointPolicyConditionAmount(),
                this.getPointPolicyRate(),
                this.getPointPolicyApplyAmount(),
                this.pointPolicyCondition,
                this.pointPolicyApplyType,
                this.pointPolicyState
        );
    }

    // PointPolicyResponse를 PointPolicyRequest로 변환하는 메서드
    public static PointPolicyRequest from(PointPolicyResponse response) {
        return PointPolicyRequest.builder()
                .pointPolicyName(response.getPointPolicyName())
                .pointPolicyRate((int) response.getPointPolicyRate())  // double을 int로 변환
                .pointPolicyConditionAmount((int) response.getPointPolicyConditionAmount())  // double을 int로 변환
                .pointPolicyApplyAmount((int) response.getPointPolicyApplyAmount())  // double을 int로 변환
                .pointPolicyCondition(response.getPointPolicyCondition())
                .pointPolicyApplyType(response.isPointPolicyApplyType())
                .build();
    }

    // PointPolicyRequest를 PointPolicy 엔티티로 변환
    public PointPolicy toEntity() {
        return PointPolicy.builder()
                .pointPolicyName(this.pointPolicyName)
                .pointPolicyConditionAmount(this.getPointPolicyConditionAmount())
                .pointPolicyRate(this.getPointPolicyRate())
                .pointPolicyApplyAmount(this.getPointPolicyApplyAmount())
                .pointPolicyCondition(this.pointPolicyCondition)
                .pointPolicyApplyType(this.pointPolicyApplyType)
                .pointPolicyState(this.pointPolicyState)
                .build();
    }

    // 각 필드에 대해 null을 처리하여 기본값을 설정하는 메서드
    public Integer getPointPolicyConditionAmount() {
        return pointPolicyConditionAmount != null ? pointPolicyConditionAmount : 0;
    }

    public Integer getPointPolicyRate() {
        return pointPolicyRate != null ? pointPolicyRate : 0;
    }

    public Integer getPointPolicyApplyAmount() {
        return pointPolicyApplyAmount != null ? pointPolicyApplyAmount : 0;
    }
}
