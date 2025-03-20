package com.nhnacademy.taskapi.order.dto;

import com.nhnacademy.taskapi.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderMemberResponse {
    private Long orderId;
    private Long memberId;
    private String orderName;
    private String ordererName;
    private String ordererPhoneNumber;
    private LocalDateTime dateTime;
    private int deliveryPrice;
    private int totalPrice;
    private String statusName;
    private int packagingPrice;

   public static OrderMemberResponse fromOrder(Order order) {
       return new OrderMemberResponse(
               order.getOrderId(),
               order.getMember().getId(),
               order.getOrderName(),
               order.getOrdererName(),
               order.getOrdererPhoneNumber(),
               order.getDateTime(),
               order.getDeliveryPrice(),
               order.getTotalPrice(),
               order.getOrderStatus().getStatusName(),
               order.getPackagingPrice()
       );
   }
}
