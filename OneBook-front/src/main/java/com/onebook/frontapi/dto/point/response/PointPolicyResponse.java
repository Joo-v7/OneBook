package com.onebook.frontapi.dto.point.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PointPolicyResponse(
        Long pointPolicyId,
        String pointPolicyName,
        int pointPolicyConditionAmount,
        int pointPolicyRate,
        int pointPolicyApplyAmount,
        String pointPolicyCondition,
        boolean pointPolicyApplyType,
        LocalDateTime pointPolicyCreatedAt,
        LocalDateTime pointPolicyUpdatedAt,
        boolean pointPolicyState
) {}
