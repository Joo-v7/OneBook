package com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UpdateRatePolicyForBookRequest {
    @NotNull(message = "id가 null일수 없습니다")
    @PositiveOrZero(message = "id는 0보다 크거나 같아야합니다")
    private Long id;
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
    @Size(max=100)
    private String name;

    @Size(max=200)
    private String description;
    @NotBlank(message = "대상 도서의 isbn13이 비어있을 수 없습니다")
    @Size(min=13, message = "isbn13 코드 13자리를 입력하셔야 합니다")
    private String bookIsbn13;

}
