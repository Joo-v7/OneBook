package com.nhnacademy.taskapi.order.controller;

import com.nhnacademy.taskapi.order.dto.OrderDetailResponse;
import com.nhnacademy.taskapi.order.service.OrderDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "OrderDetail", description = "주문에 있는 각 주문상세내역을 조회")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @GetMapping("/task/order-detail/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@RequestHeader("X-MEMBER-ID") Long memberId, @PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(orderDetailService.getOrderDetail(memberId, orderId));
    }

}
