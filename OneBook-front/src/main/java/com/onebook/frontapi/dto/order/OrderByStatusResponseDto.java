package com.onebook.frontapi.dto.order;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class OrderByStatusResponseDto {
    // 주문id
    // 회원id
    // 주문상태
    // 주문이름
    // 휴대폰번호
    // 주문일시
    // 배송비
    // 주문금액
    Long orderId;
    Long memberId;
    String orderStatus;
    String orderName;
    String orderer;
    String phoneNumber;
    LocalDateTime orderTime;
    LocalDate deliveryTime;
    int deliveryPrice;
    int totalPrice;
}
