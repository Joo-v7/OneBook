package com.nhnacademy.taskapi.coupon.domain.entity.policies;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddPricePolicyForBookRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdatePricePolicyForBookRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdateRatePolicyForBookRequest;
import com.nhnacademy.taskapi.coupon.domain.entity.status.PolicyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "price_policies_for_book")
@Getter
@NoArgsConstructor
public class PricePolicyForBook  implements Policy{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pricePolicyForBookId;

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
    @JoinColumn(name = "book_id",nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "policy_status_id",nullable = false)
    private PolicyStatus policyStatus;

    public PricePolicyForBook(Integer minimumOrderAmount, Integer discountPrice, LocalDateTime expirationPeriodStart,
                              LocalDateTime expirationPeriodEnd, String name, String description, Book book,
                              PolicyStatus policyStatus) {
        this.minimumOrderAmount = minimumOrderAmount;
        this.discountPrice = discountPrice;
        this.expirationPeriodStart = expirationPeriodStart;
        this.expirationPeriodEnd = expirationPeriodEnd;
        this.name = name;
        this.description = description;
        this.book = book;
        this.policyStatus = policyStatus;
    }

    public static PricePolicyForBook createPricePolicyForBook(AddPricePolicyForBookRequest addPricePolicyForBookRequest,
                                                              Book book, PolicyStatus policyStatus) {
        return new PricePolicyForBook(
                addPricePolicyForBookRequest.getMinimumOrderAmount(),
                addPricePolicyForBookRequest.getDiscountPrice(),
                addPricePolicyForBookRequest.getExpirationPeriodStart(),
                addPricePolicyForBookRequest.getExpirationPeriodEnd(),
                addPricePolicyForBookRequest.getName(),
                addPricePolicyForBookRequest.getDescription(),
                book,
                policyStatus
        );
    }

    public void updatePolicy(UpdatePricePolicyForBookRequest updatePricePolicyForBookRequest, Book book){
        minimumOrderAmount = updatePricePolicyForBookRequest.getMinimumOrderAmount();
        discountPrice = updatePricePolicyForBookRequest.getDiscountPrice();
        expirationPeriodStart = updatePricePolicyForBookRequest.getExpirationPeriodStart();
        expirationPeriodEnd = updatePricePolicyForBookRequest.getExpirationPeriodEnd();
        name = updatePricePolicyForBookRequest.getName();
        description = updatePricePolicyForBookRequest.getDescription();
        this.book = book;
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
