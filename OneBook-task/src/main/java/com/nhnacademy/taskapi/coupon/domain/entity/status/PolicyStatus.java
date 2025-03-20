package com.nhnacademy.taskapi.coupon.domain.entity.status;

import com.nhnacademy.taskapi.coupon.domain.dto.status.request.AddPolicyStatusRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PolicyStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer policyStatusId;

    @Column(nullable = false, length = 30)
    private String name;

    public PolicyStatus(String name) {
        this.name = name;
    }

    public static PolicyStatus createPolicyStatus(AddPolicyStatusRequest addPolicyStatusRequest){
        return new PolicyStatus(addPolicyStatusRequest.getName());
    }
}
