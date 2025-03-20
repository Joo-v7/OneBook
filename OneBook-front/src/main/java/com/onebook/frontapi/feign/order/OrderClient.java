package com.onebook.frontapi.feign.order;

import com.onebook.frontapi.dto.order.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "OrderClient", url = "${onebook.gatewayUrl}")
public interface OrderClient {
    // 주문 생성
    @PostMapping("/task/order")
    Long createOrder(@RequestBody OrderFormRequest orderFormRequest);

    // 주문ID를 이용한 주문 찾기
    @GetMapping("/task/orders/{order-id}")
    ResponseEntity<OrderMemberFeignResponse> findOrderByOrderId(@PathVariable("order-id") Long orderId);

    // 사용자ID를 이용한 주문 찾기
    @GetMapping("/task/orders")
    ResponseEntity<Page<OrderFeignResponse>> findOrders(@RequestParam(name = "order-status", required = false) String orderStatus, Pageable pageable);

    // 주문 상태에 따른 주문 리스트 불러오기
    @GetMapping("/task/admin/orders")
    List<OrderByStatusResponseDto> findOrderByStatus(@RequestParam("status") String status);

    // 주문 상태 변경하기
    @PutMapping("/task/orders")
    void updateOrderStatus(@RequestBody List<Long> orderIds, @RequestParam String status);

    // 주문 상세 정보 확인하기
    @GetMapping("/task/order-detail/{orderId}")
    OrderDetailFeignResponse findOrderDetail(@PathVariable("orderId") Long orderId);

    // 교환, 반품&환불 저장
    @PostMapping("/task/solutions/order/{order-id}")
    void saveOrderSolution(@PathVariable("order-id") Long orderId, @RequestBody OrderSolutionRequest orderSolutionRequest);

}