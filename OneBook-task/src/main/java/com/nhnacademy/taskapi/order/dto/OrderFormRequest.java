package com.nhnacademy.taskapi.order.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderFormRequest {
    private List<BookOrderRequest> items;
    private DeliveryRequest delivery;
    private int packagingId;
}
