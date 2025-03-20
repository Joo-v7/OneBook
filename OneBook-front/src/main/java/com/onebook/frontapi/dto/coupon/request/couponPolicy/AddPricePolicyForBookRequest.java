package com.onebook.frontapi.dto.coupon.request.couponPolicy;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class AddPricePolicyForBookRequest {
    @NotNull
    private Integer minimumOrderAmount;
    @NotNull
    private Integer discountPrice;
    @NotNull
    private LocalDateTime expirationPeriodStart;
    @NotNull
    private LocalDateTime expirationPeriodEnd;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String bookIsbn13;
}
