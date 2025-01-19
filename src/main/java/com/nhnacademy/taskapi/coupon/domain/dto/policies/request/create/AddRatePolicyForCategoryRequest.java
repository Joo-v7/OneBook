package com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddRatePolicyForCategoryRequest {

    @NotNull(message = "할인율을 입력하세요")
    @Min(value = 0, message = "할인율은 0보다 작을수 없습니다")
    @Max(value = 100,message = "할인율은 100보다 클 수 없습니다.")
    private Integer discountRate;

    @NotNull(message = "최소주문금액은 비어있을 수 없습니다")
    private Integer minimumOrderAmount;

    @NotNull(message = "최대할인금액은 비어있을 수 없습니다")
    private Integer maximumDiscountPrice;

    @NotNull(message = "유효기간 start, 비어있을 수 없습니다")
    private LocalDateTime expirationPeriodStart;

    @NotNull(message = "유효기간 end, 비어있을 수 없습니다")
    private LocalDateTime expirationPeriodEnd;

    @NotBlank(message = "정책의 이름이 비어있을 수 없습니다")
    private String name;

    private String description;

    @NotNull(message = "대상 카테고리의 id를 입력하세요")
    private Integer categoryId;
}
