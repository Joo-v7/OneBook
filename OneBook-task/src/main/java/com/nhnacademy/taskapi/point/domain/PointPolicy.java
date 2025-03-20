package com.nhnacademy.taskapi.point.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "point_policies")
public class PointPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointPolicyId;

    @NotNull(message = "포인트 정책명은 필수입니다.")
    @Column(nullable = false, length = 50)
    private String pointPolicyName;

    private Integer pointPolicyConditionAmount;
    private Integer pointPolicyRate;
    private Integer pointPolicyApplyAmount;

    @NotNull(message = "포인트 적립 조건은 필수입니다.")
    @Column(nullable = false, length = 200)
    private String pointPolicyCondition;

    @NotNull(message = "포인트 적립 유형은 필수입니다.")
    @Column(nullable = false)
    private boolean pointPolicyApplyType;

    @NotNull(message = "포인트 생성일은 필수입니다.")
    @Column(nullable = false)
    private LocalDateTime pointPolicyCreatedAt;

    private LocalDateTime pointPolicyUpdatedAt;

    @NotNull(message = "포인트 상태는 필수입니다.")
    @Column(nullable = false)
    private boolean pointPolicyState;

    @Builder
    public PointPolicy(Long pointPolicyId, String pointPolicyName, Integer pointPolicyRate, Integer pointPolicyConditionAmount,
                       String pointPolicyCondition, Integer pointPolicyApplyAmount, LocalDateTime pointPolicyCreatedAt,
                       LocalDateTime pointPolicyUpdatedAt, boolean pointPolicyApplyType, boolean pointPolicyState) {
        this.pointPolicyId = pointPolicyId;
        this.pointPolicyName = pointPolicyName;
        this.pointPolicyConditionAmount = pointPolicyConditionAmount;
        this.pointPolicyRate = pointPolicyRate;
        this.pointPolicyCondition = pointPolicyCondition;
        this.pointPolicyApplyAmount = pointPolicyApplyAmount;
        this.pointPolicyApplyType = pointPolicyApplyType;
        this.pointPolicyCreatedAt = pointPolicyCreatedAt;
        this.pointPolicyUpdatedAt = pointPolicyUpdatedAt;
        this.pointPolicyState = pointPolicyState;
    }

    // @PrePersist와 @PreUpdate를 이용하여 생성 및 수정 일자를 자동으로 처리
    @PrePersist
    public void prePersist() {
        this.pointPolicyCreatedAt = LocalDateTime.now();
        this.pointPolicyUpdatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.pointPolicyUpdatedAt = LocalDateTime.now();
    }

    // 필드를 업데이트하는 메서드
    public void updatePointPolicyName(String pointPolicyName) {
        this.pointPolicyName = pointPolicyName;
    }

    public void updatePointPolicyConditionAmount(Integer pointPolicyConditionAmount) {
        this.pointPolicyConditionAmount = pointPolicyConditionAmount;
    }

    public void updatePointPolicyApplyAmount(Integer pointPolicyApplyAmount) {
        this.pointPolicyApplyAmount = pointPolicyApplyAmount;
    }

    public void updatePointPolicyRate(Integer pointPolicyRate) {
        this.pointPolicyRate = pointPolicyRate;
    }

    public void updatePointPolicyCondition(String pointPolicyCondition) {
        this.pointPolicyCondition = pointPolicyCondition;
    }

    public void updatePointPolicyApplyType(boolean pointPolicyApplyType) {
        this.pointPolicyApplyType = pointPolicyApplyType;
    }

    public void updatePointPolicyState(boolean pointPolicyState) {
        this.pointPolicyState = pointPolicyState;
    }

    public void updatePointPolicyCreatedAt(LocalDateTime pointPolicyCreatedAt) {
        this.pointPolicyCreatedAt = pointPolicyCreatedAt;

    }
}