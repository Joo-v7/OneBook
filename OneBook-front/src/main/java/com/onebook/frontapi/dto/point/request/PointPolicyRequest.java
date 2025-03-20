package com.onebook.frontapi.dto.point.request;

import com.onebook.frontapi.dto.point.response.PointPolicyResponse;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointPolicyRequest {
    private Long pointPolicyId;
    private String pointPolicyName;
    private Integer pointPolicyRate;
    private Integer pointPolicyConditionAmount;
    private Integer pointPolicyApplyAmount;
    private String pointPolicyCondition;
    private boolean pointPolicyApplyType;

    public static PointPolicyRequest from(PointPolicyResponse response) {
        return PointPolicyRequest.builder()
                .pointPolicyId(response.pointPolicyId())  // pointPolicyId 포함
                .pointPolicyName(response.pointPolicyName())
                .pointPolicyRate(response.pointPolicyRate())
                .pointPolicyConditionAmount(response.pointPolicyConditionAmount())
                .pointPolicyApplyAmount(response.pointPolicyApplyAmount())
                .pointPolicyCondition(response.pointPolicyCondition())
                .pointPolicyApplyType(response.pointPolicyApplyType())
                .build();
    }
}
