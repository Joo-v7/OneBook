package com.onebook.frontapi.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OrderRegisterRequest {
    String orderer;
    String phoneNumber;
    LocalDateTime dateTime;
    int deliveryPrice;
    int totalPrice;
}
