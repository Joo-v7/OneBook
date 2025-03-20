package com.onebook.frontapi.dto.point.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointUpdateRequest {

    private int amount; // 포인트 금액
    private String updatedType; // 업데이트 타입 (적립, 차감 등)
}
