package com.nhnacademy.taskapi.delivery.service.impl;

import com.nhnacademy.taskapi.delivery.entity.Delivery;
import com.nhnacademy.taskapi.delivery.repository.DeliveryRepository;
import com.nhnacademy.taskapi.delivery.service.DeliveryService;
import com.nhnacademy.taskapi.order.dto.DeliveryRequest;
import com.nhnacademy.taskapi.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;

    @Override
    public void createDelivery(Order order, DeliveryRequest deliveryRequest) {
        Delivery delivery = new Delivery(
                order,
                deliveryRequest.getRecipientName(),
                deliveryRequest.getOrdererPhoneNumber(),
                deliveryRequest.getRecipientAddress(),
                deliveryRequest.getRecipientRequestedTerm(),
                deliveryRequest.getDeliveryCompletedDate()
        );

        deliveryRepository.save(delivery);
    }
}
