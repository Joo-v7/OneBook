package com.nhnacademy.taskapi.order.dto;

import com.nhnacademy.taskapi.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderStatusResponse {
    // 주문id
    Long orderId;
    // 회원id
    Long memberId;
    // 주문상태
    String memberName;
    String orderStatus;
    // 주문이름
    String orderName;
    // 주문자 이름
    String ordererName;
    // 휴대폰번호
    String phoneNumber;
    // 주문일시
    LocalDateTime orderTime;
//     배송 예상 시간
//    LocalDate deliveryExpectedDate;
    // 배송비
    int deliveryPrice;
    // 주문금액
    int totalPrice;

    public static OrderStatusResponse fromOrderStatus(Order order) {
        return new OrderStatusResponse(
                order.getOrderId(),
                order.getMember().getId(),
                order.getMember().getName(),
                order.getOrderStatus().getStatusName(),
                order.getOrderName(),
                order.getOrdererName(),
                order.getOrdererPhoneNumber(),
                order.getDateTime(),
                order.getDeliveryPrice(),
                order.getTotalPrice());
    }
}
