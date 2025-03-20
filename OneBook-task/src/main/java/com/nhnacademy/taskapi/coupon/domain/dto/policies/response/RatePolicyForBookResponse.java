package com.nhnacademy.taskapi.coupon.domain.dto.policies.response;

import com.nhnacademy.taskapi.coupon.domain.entity.policies.RatePolicyForBook;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class RatePolicyForBookResponse {

    private Long id;
    private Integer discountRate;
    private Integer minimumOrderAmount;
    private Integer maximumDiscountPrice;
    private LocalDateTime expirationPeriodStart;
    private LocalDateTime expirationPeriodEnd;
    private String name;
    private String description;
    private String bookName;
    private String bookIsbn13;
    private String policyStatusName;

    public static RatePolicyForBookResponse changeEntityToDto(RatePolicyForBook ratePolicyForBook){
        return new RatePolicyForBookResponse(
                ratePolicyForBook.getRatePolicyForBookId(),
                ratePolicyForBook.getDiscountRate(),
                ratePolicyForBook.getMinimumOrderAmount(),
                ratePolicyForBook.getMaximumDiscountPrice(),
                ratePolicyForBook.getExpirationPeriodStart(),
                ratePolicyForBook.getExpirationPeriodEnd(),
                ratePolicyForBook.getName(),
                ratePolicyForBook.getDescription(),
                ratePolicyForBook.getBook().getTitle(),
                ratePolicyForBook.getBook().getIsbn13(),
                ratePolicyForBook.getPolicyStatus().getName()
        );
    }

}
