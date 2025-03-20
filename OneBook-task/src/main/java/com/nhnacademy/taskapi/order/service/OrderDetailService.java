package com.nhnacademy.taskapi.order.service;

import com.nhnacademy.taskapi.order.dto.BookOrderRequest;
import com.nhnacademy.taskapi.order.dto.OrderDetailResponse;
import com.nhnacademy.taskapi.order.entity.Order;

import java.util.List;

public interface OrderDetailService {
    void saveOrderDetail(Order order, List<BookOrderRequest> bookOrderRequests);
    OrderDetailResponse getOrderDetail(Long memberId, Long orderId);
}
