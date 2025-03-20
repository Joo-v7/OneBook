package com.nhnacademy.taskapi.order.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class DeliveryRequest {
    String ordererName;
    String ordererPhoneNumber;
    String recipientAddress;
    String recipientRequestedTerm;
    String recipientName;
    String recipientPhone;
    LocalDate deliveryCompletedDate;
}
