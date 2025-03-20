package com.nhnacademy.taskapi.order.controller;

import com.nhnacademy.taskapi.order.service.OrderStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "OrderStatus", description = "주문에 대한 상태 조회")
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    @GetMapping("/task/order/statuses")
    public ResponseEntity<List<String>> getOrderStatuses() {
        return ResponseEntity.ok(orderStatusService.getOrderStatusesList());
    }
}
