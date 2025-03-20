package com.onebook.frontapi.dto.coupon.request.couponPolicy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AddRatePolicyForBookRequest {
    @NotNull
    private Integer discountRate;
    @NotNull
    private Integer minimumOrderAmount;
    @NotNull
    private Integer maximumDiscountPrice;
    @NotNull
    private LocalDateTime expirationPeriodStart;
    @NotNull
    private LocalDateTime expirationPeriodEnd;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotBlank
    private String bookIsbn13;
}
