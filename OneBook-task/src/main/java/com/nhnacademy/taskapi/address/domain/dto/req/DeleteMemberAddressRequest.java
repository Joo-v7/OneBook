package com.nhnacademy.taskapi.address.domain.dto.req;

import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DeleteMemberAddressRequest {

    @NotNull(message = "id가 비어있습니다")
    @PositiveOrZero(message = "id는 0보다 작을 수 없습니다")
    private Long id;
}
