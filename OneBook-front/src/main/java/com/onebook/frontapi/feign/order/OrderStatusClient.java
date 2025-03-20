package com.onebook.frontapi.feign.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "OrderStatusClient", url = "${onebook.gatewayUrl}")
public interface OrderStatusClient {
    // 주문 상태 리스트 불러오기
    @GetMapping("/task/order/statuses")
    List<String> findAllOrderStatuses();
}
