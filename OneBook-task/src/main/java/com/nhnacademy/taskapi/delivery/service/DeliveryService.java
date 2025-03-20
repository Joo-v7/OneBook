package com.nhnacademy.taskapi.delivery.service;

import com.nhnacademy.taskapi.delivery.entity.Delivery;
import com.nhnacademy.taskapi.order.dto.DeliveryRequest;
import com.nhnacademy.taskapi.order.entity.Order;

public interface DeliveryService {
    void createDelivery(Order order, DeliveryRequest deliveryRequest);
}
