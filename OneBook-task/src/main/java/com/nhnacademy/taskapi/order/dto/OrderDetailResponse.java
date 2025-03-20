package com.nhnacademy.taskapi.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private List<OrderDetailBookResponse> items;
    private OrderResponse orderResponse;
}
