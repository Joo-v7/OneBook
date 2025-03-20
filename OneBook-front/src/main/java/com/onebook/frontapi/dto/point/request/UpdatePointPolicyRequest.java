package com.onebook.frontapi.dto.point.request;

import com.onebook.frontapi.dto.point.response.PointPolicyResponse;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePointPolicyRequest {
    private Long pointPolicyId;
    private String pointPolicyName;
    private Integer pointPolicyRate;
    private Integer pointPolicyConditionAmount;
    private Integer pointPolicyApplyAmount;
    private String pointPolicyCondition;
    private Boolean pointPolicyApplyType;
    private Boolean pointPolicyState;

    public static UpdatePointPolicyRequest from(PointPolicyResponse response) {
        return UpdatePointPolicyRequest.builder()
                .pointPolicyId(response.pointPolicyId())
                .pointPolicyName(response.pointPolicyName())
                .pointPolicyRate(response.pointPolicyRate())
                .pointPolicyConditionAmount(response.pointPolicyConditionAmount())
                .pointPolicyApplyAmount(response.pointPolicyApplyAmount())
                .pointPolicyCondition(response.pointPolicyCondition())
                .pointPolicyApplyType(response.pointPolicyApplyType())
                .pointPolicyState(response.pointPolicyState())
                .build();
    }
}
