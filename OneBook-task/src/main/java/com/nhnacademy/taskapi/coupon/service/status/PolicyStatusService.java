package com.nhnacademy.taskapi.coupon.service.status;

import com.nhnacademy.taskapi.coupon.domain.dto.status.request.AddPolicyStatusRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.status.request.GetPolicyStatusRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.status.response.AddPolicyStatusResponse;
import com.nhnacademy.taskapi.coupon.domain.dto.status.response.GetPolicyStatusResponse;
import com.nhnacademy.taskapi.coupon.domain.entity.status.PolicyStatus;
import com.nhnacademy.taskapi.coupon.repository.status.PolicyStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyStatusService {

    private final PolicyStatusRepository policyStatusRepository;

    public AddPolicyStatusResponse addPolicyStatus(AddPolicyStatusRequest addPolicyStatusRequest){

        PolicyStatus policyStatus = policyStatusRepository.save(PolicyStatus.createPolicyStatus(addPolicyStatusRequest));
        return AddPolicyStatusResponse.changeEntityToDto(policyStatus);
    }

    public GetPolicyStatusResponse getPolicyStatus(GetPolicyStatusRequest getPolicyStatusRequest){

        PolicyStatus policyStatus = policyStatusRepository.findById(getPolicyStatusRequest.getId()).orElseThrow();
        return GetPolicyStatusResponse.changeEntityToDto(policyStatus);
    }

    public void deleteAll(){
        policyStatusRepository.deleteAll();
    }

}
