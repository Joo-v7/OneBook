package com.nhnacademy.taskapi.order.service;

import com.nhnacademy.taskapi.order.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    long processOrder(Long memberId, OrderFormRequest orderFormRequest);

    Page<OrderResponse> getOrderList(Long memberId, Pageable pageable);
    Page<OrderResponse> getOrderListByStatusName(Long memberId, String statusName, Pageable pageable);
    Page<OrderResponse> getOrderListExcludePending(Long memberId, String statusName, Pageable pageable);
    Page<OrderMemberResponse> getOrders(Long memberId, String statusName, Pageable pageable);

    List<OrderStatusResponse> getOrdersByStatusName(String statusName);
    OrdererResponseDto getOrderer(Long memberId);
    void updateOrderStatus(List<Long> orderIds, String status);
    public OrderMemberResponse getOrder(Long orderId);
}
