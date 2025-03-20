package com.onebook.frontapi.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class OrderDetailResponse {
    private List<OrderDetailBookFeignResponse> items;
    private OrderResponse orderResponse;
}
