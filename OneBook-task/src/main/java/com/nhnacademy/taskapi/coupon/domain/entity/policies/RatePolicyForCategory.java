package com.nhnacademy.taskapi.coupon.domain.entity.policies;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddRatePolicyForCategoryRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdateRatePolicyForBookRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdateRatePolicyForCategoryRequest;
import com.nhnacademy.taskapi.coupon.domain.entity.status.PolicyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "rate_policies_for_category")
@Getter
@NoArgsConstructor
public class RatePolicyForCategory implements Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratePolicyForCategoryId;

    @Column(nullable = false)
    private Integer discountRate;

    private Integer minimumOrderAmount;

    private Integer maximumDiscountPrice;

    @Column(nullable = false)
    private LocalDateTime expirationPeriodStart;

    @Column(nullable = false)
    private LocalDateTime expirationPeriodEnd;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 300)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "policy_status_id", nullable = false)
    private PolicyStatus policyStatus;

    public RatePolicyForCategory(Integer discountRate, Integer minimumOrderAmount, Integer maximumDiscountPrice,
                                 LocalDateTime expirationPeriodStart, LocalDateTime expirationPeriodEnd, String name,
                                 String description, Category category, PolicyStatus policyStatus) {
        this.discountRate = discountRate;
        this.minimumOrderAmount = minimumOrderAmount;
        this.maximumDiscountPrice = maximumDiscountPrice;
        this.expirationPeriodStart = expirationPeriodStart;
        this.expirationPeriodEnd = expirationPeriodEnd;
        this.name = name;
        this.description = description;
        this.category = category;
        this.policyStatus = policyStatus;
    }

    public static RatePolicyForCategory createRatePolicyForCategory(AddRatePolicyForCategoryRequest addRatePolicyForCategoryRequest,
                                                                Category category, PolicyStatus policyStatus)
    {
        return new RatePolicyForCategory(
                addRatePolicyForCategoryRequest.getDiscountRate(),
                addRatePolicyForCategoryRequest.getMinimumOrderAmount(),
                addRatePolicyForCategoryRequest.getMaximumDiscountPrice(),
                addRatePolicyForCategoryRequest.getExpirationPeriodStart(),
                addRatePolicyForCategoryRequest.getExpirationPeriodEnd(),
                addRatePolicyForCategoryRequest.getName(),
                addRatePolicyForCategoryRequest.getDescription(),
                category,
                policyStatus
        );
    }

    public void updatePolicy(UpdateRatePolicyForCategoryRequest updateRatePolicyForCategoryRequest, Category category){
        discountRate = updateRatePolicyForCategoryRequest.getDiscountRate();
        minimumOrderAmount = updateRatePolicyForCategoryRequest.getMinimumOrderAmount();
        maximumDiscountPrice = updateRatePolicyForCategoryRequest.getMaximumDiscountPrice();
        expirationPeriodStart = updateRatePolicyForCategoryRequest.getExpirationPeriodStart();
        expirationPeriodEnd = updateRatePolicyForCategoryRequest.getExpirationPeriodEnd();
        name = updateRatePolicyForCategoryRequest.getName();
        description = updateRatePolicyForCategoryRequest.getDescription();
        this.category = category;
    }

    // 사용되고 있는 쿠폰 정책을 삭제할때는 policyStatus를 "삭제됨" 으로 업뎃
    // 사용되지 않고있는 쿠폰 정책이라면 삭제할때 그냥 db에서 삭제
    public void deletePolicy(PolicyStatus policyStatus){
        this.policyStatus = policyStatus;
    }

    // 쿠폰 정책이, 어느 쿠폰에 적용이 되면 usePolicy 메서드를 사용해 policyStatus 변경
    public void usePolicy(PolicyStatus policyStatus){
        this.policyStatus = policyStatus;
    }
}
