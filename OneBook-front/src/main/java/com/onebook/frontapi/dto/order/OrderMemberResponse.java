package com.onebook.frontapi.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderMemberResponse {
    Long orderId;
    Long memberId;

    public static OrderMemberResponse fromFeign(OrderMemberFeignResponse order) {
        return new OrderMemberResponse(
                order.getOrderId(),
                order.getMemberId()
        );
    }
}
