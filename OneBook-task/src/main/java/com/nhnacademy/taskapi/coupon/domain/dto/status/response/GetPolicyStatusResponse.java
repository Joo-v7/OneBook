package com.nhnacademy.taskapi.coupon.domain.dto.status.response;

import com.nhnacademy.taskapi.coupon.domain.entity.status.PolicyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetPolicyStatusResponse {
    String name;

    public static GetPolicyStatusResponse changeEntityToDto(PolicyStatus policyStatus){
        return new GetPolicyStatusResponse(policyStatus.getName());
    }
}
