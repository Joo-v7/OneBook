package com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UpdatePricePolicyForCategoryRequest {

    @NotNull(message = "id가 null일수 없습니다")
    @PositiveOrZero(message = "id는 0보다 크거나 같아야합니다")
    private Long id;
    @NotNull(message = "최소주문금액은 비어있을 수 없습니다")
    private Integer minimumOrderAmount;
    @NotNull(message = "할인금액은 비어있을 수 없습니다")
    private Integer discountPrice;
    @NotNull(message = "유효기간 start, 비어있을 수 없습니다")
    private LocalDateTime expirationPeriodStart;
    @NotNull(message = "유효기간 end, 비어있을 수 없습니다")
    private LocalDateTime expirationPeriodEnd;
    @NotBlank(message = "정책의 이름이 비어있을 수 없습니다")
    @Size(max=100)
    private String name;
    @Size(max=200)
    private String description;
    @NotNull(message = "대상 카테고리의 id를 입력하세요")
    private Integer categoryId;
//    @NotNull
//    private Integer policyStatusId;
}
