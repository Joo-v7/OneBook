package com.onebook.frontapi.dto.point.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePointPolicyRequest {
    private String pointPolicyName;
    private int pointPolicyRate;
    private Integer pointPolicyConditionAmount;
    private Integer pointPolicyApplyAmount;
    private String pointPolicyCondition;
    private boolean pointPolicyApplyType;
}