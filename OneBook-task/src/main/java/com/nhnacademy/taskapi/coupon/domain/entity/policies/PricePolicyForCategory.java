package com.nhnacademy.taskapi.coupon.domain.entity.policies;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddPricePolicyForCategoryRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdatePricePolicyForBookRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdatePricePolicyForCategoryRequest;
import com.nhnacademy.taskapi.coupon.domain.entity.status.PolicyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "price_policies_for_category")
@Getter
@NoArgsConstructor
public class PricePolicyForCategory implements Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pricePolicyForCategoryId;

    private Integer minimumOrderAmount;

    @Column(nullable = false)
    private Integer discountPrice;

    @Column(nullable = false)
    private LocalDateTime expirationPeriodStart;

    @Column(nullable = false)
    private LocalDateTime expirationPeriodEnd;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 300)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "policy_status_id",nullable = false)
    private PolicyStatus policyStatus;

    public PricePolicyForCategory(Integer minimumOrderAmount, Integer discountPrice, LocalDateTime expirationPeriodStart,
                                  LocalDateTime expirationPeriodEnd, String name, String description, Category category,
                                  PolicyStatus policyStatus) {
        this.minimumOrderAmount = minimumOrderAmount;
        this.discountPrice = discountPrice;
        this.expirationPeriodStart = expirationPeriodStart;
        this.expirationPeriodEnd = expirationPeriodEnd;
        this.name = name;
        this.description = description;
        this.category = category;
        this.policyStatus = policyStatus;
    }

    public static PricePolicyForCategory createPricePolicyForCategory(AddPricePolicyForCategoryRequest addPricePolicyForCategoryRequest,
                                                                      Category category, PolicyStatus policyStatus){

        return new PricePolicyForCategory(
                addPricePolicyForCategoryRequest.getMinimumOrderAmount(),
                addPricePolicyForCategoryRequest.getDiscountPrice(),
                addPricePolicyForCategoryRequest.getExpirationPeriodStart(),
                addPricePolicyForCategoryRequest.getExpirationPeriodEnd(),
                addPricePolicyForCategoryRequest.getName(),
                addPricePolicyForCategoryRequest.getDescription(),
                category,
                policyStatus
        );
    }

    public void updatePolicy(UpdatePricePolicyForCategoryRequest updatePricePolicyForCategoryRequest, Category category){
        minimumOrderAmount = updatePricePolicyForCategoryRequest.getMinimumOrderAmount();
        discountPrice = updatePricePolicyForCategoryRequest.getDiscountPrice();
        expirationPeriodStart = updatePricePolicyForCategoryRequest.getExpirationPeriodStart();
        expirationPeriodEnd = updatePricePolicyForCategoryRequest.getExpirationPeriodEnd();
        name = updatePricePolicyForCategoryRequest.getName();
        description = updatePricePolicyForCategoryRequest.getDescription();
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
