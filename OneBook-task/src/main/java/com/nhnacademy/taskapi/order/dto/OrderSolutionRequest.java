package com.nhnacademy.taskapi.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderSolutionRequest {
    private static final long serialVersionUID = 1L; // 버전 관리를 위한 ID

    private String solutionType;
    private List<BookOrderRequest> items;
    private String reason;

    public OrderSolutionRequest(String solutionType, List<BookOrderRequest> items) {
        this.solutionType = solutionType;
        this.items = items;
    }
}
