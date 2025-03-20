package com.nhnacademy.taskapi.coupon.domain.dto.status.response;


import com.nhnacademy.taskapi.coupon.domain.entity.status.PolicyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddPolicyStatusResponse {

    private String name;

    public static AddPolicyStatusResponse changeEntityToDto(PolicyStatus policyStatus){
        return new AddPolicyStatusResponse(policyStatus.getName());
    }
}
