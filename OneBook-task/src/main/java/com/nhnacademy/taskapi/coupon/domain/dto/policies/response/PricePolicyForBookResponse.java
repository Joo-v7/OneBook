package com.nhnacademy.taskapi.coupon.domain.dto.policies.response;

import com.nhnacademy.taskapi.coupon.domain.entity.policies.PricePolicyForBook;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class PricePolicyForBookResponse {

    private Long id;
    private Integer minimumOrderAmount;
    private Integer discountPrice;
    private LocalDateTime expirationPeriodStart;
    private LocalDateTime expirationPeriodEnd;
    private String name;
    private String description;
    private String bookName;
    private String bookIsbn13;
    private String policyStatusName;

    public static PricePolicyForBookResponse changeEntityToDto(PricePolicyForBook pricePolicyForBook){
        return new PricePolicyForBookResponse(
                pricePolicyForBook.getPricePolicyForBookId(),
                pricePolicyForBook.getMinimumOrderAmount(),
                pricePolicyForBook.getDiscountPrice(),
                pricePolicyForBook.getExpirationPeriodStart(),
                pricePolicyForBook.getExpirationPeriodEnd(),
                pricePolicyForBook.getName(),
                pricePolicyForBook.getDescription(),
                pricePolicyForBook.getBook().getTitle(),
                pricePolicyForBook.getBook().getIsbn13(),
                pricePolicyForBook.getPolicyStatus().getName()
        );
    }

}
