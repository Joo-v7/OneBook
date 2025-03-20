package com.onebook.frontapi.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderDetailFeignResponse {
    private List<OrderDetailBookFeignResponse> items;
    private OrderFeignResponse orderResponse;
}
