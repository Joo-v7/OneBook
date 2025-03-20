package com.onebook.frontapi.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class OrderResponse {
    private long orderId;
    private String orderName;
    private String orderer;
    private LocalDateTime dateTime;
    private int totalPrice;
    private String orderStatusName;

    public static OrderResponse fromFeign(OrderFeignResponse feignResponse) {
        return new OrderResponse(
                feignResponse.getOrderId(),
                feignResponse.getOrderName(),
                feignResponse.getOrdererName(),
                feignResponse.getDateTime(),
                feignResponse.getTotalPrice(),
                feignResponse.getOrderStatusName()
        );
    }

    public static OrderResponse fromFeign(OrderMemberFeignResponse feignResponse) {
        return new OrderResponse(
                feignResponse.getOrderId(),
                feignResponse.getOrderName(),
                feignResponse.getOrdererName(),
                feignResponse.getDateTime(),
                feignResponse.getTotalPrice(),
                feignResponse.getStatusName()
        );
    }
}
