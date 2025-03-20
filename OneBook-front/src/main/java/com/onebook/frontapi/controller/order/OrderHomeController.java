package com.onebook.frontapi.controller.order;

import com.onebook.frontapi.dto.order.OrderDetailResponse;
import com.onebook.frontapi.service.order.OrderService;
import com.onebook.frontapi.service.order.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class OrderHomeController {

    private final OrderService orderService;
    private final OrderStatusService orderStatusService;

    @GetMapping("/my/orders")
    public String myOrders(@RequestParam(name = "order-status", required = false) String orderStatus, Pageable pageable, Model model) {
        // 결제대기 제외
        if (orderStatus == null) {
            orderStatus = "결제대기제외";
        }

        model.addAttribute("orderStatusList", orderStatusService.getAllOrderStatuses());

        // 내림차순 정렬을 위해 Sort 설정
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("orderId")));
        model.addAttribute("orderList", orderService.getOrdersByStatus(orderStatus, sortedPageable));

        if (orderStatus.equals("결제대기제외")) {
            return "order/my-orders";
        } else if (orderStatus.equals("결제대기")) {
            return "order/my-waiting-orders";
        }

        return null;
    }

    @GetMapping("/my/order-detail/{orderId}")
    public String myOrderDetail(@PathVariable("orderId") long orderId, Model model) {
        OrderDetailResponse orderDetail = orderService.getOrderDetail(orderId);

        model.addAttribute("orderDetail", orderDetail);

        return "order/order-detail";
    }

    // 반품
    @GetMapping("/my/order/cancel/{order-id}")
    public String myOrderReturn(@PathVariable(name = "order-id") Long orderId) {
        List<Long> orderIds = new ArrayList<>();
        orderIds.add(orderId);
        String postStatus = "주문취소";

        orderService.updateOrderStatus(orderIds, postStatus);
        return "redirect:/my/orders";
    }
}
