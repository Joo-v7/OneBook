package com.nhnacademy.taskapi.order.dto;

import com.nhnacademy.taskapi.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private long orderId;
    private String orderName;
    private String ordererName;
    private LocalDateTime dateTime;
    private int totalPrice;
    private String orderStatusName;
    private long memberId;

    // TODO 파라미터 변경으로 인해 생길 수 있는 에러 찾아야됨
    public static OrderResponse fromOrder(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getOrderName(),
                order.getOrdererName(),
                order.getDateTime(),
                order.getTotalPrice(),
                order.getOrderStatus().getStatusName(),
                order.getMember().getId()
        );
    }
}