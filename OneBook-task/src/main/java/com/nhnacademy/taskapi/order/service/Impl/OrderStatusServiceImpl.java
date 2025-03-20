package com.nhnacademy.taskapi.order.service.Impl;

import com.nhnacademy.taskapi.order.entity.OrderStatus;
import com.nhnacademy.taskapi.order.repository.OrderStatusRepository;
import com.nhnacademy.taskapi.order.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderStatusServiceImpl implements OrderStatusService {
    private final OrderStatusRepository orderStatusRepository;

    // create

    // read
    @Override
    public List<String> getOrderStatusesList() {
        List<String> orderResponseList = new ArrayList<>();

        for (OrderStatus orderStatus : orderStatusRepository.findAll()) {
            orderResponseList.add(orderStatus.getStatusName());
        }

        /*
        orderStatusName을 담은 객체를 넘길까? List<String>에 담아서 넘길까?
        return orderStatusRepository.findAll().stream().map(OrderStatusResponse::fromOrderStatus).toList();
         */
        return orderResponseList;
    }
}
