package com.nhnacademy.taskapi.order.controller;

import com.nhnacademy.taskapi.order.dto.OrderSolutionRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// TODO 해결
//@Controller
public class OrderSolutionController {

//    @PostMapping("/task/solutions/order/{order-id}")
    public void createOrderSolution(@PathVariable("order-id") String orderId, @RequestBody OrderSolutionRequest orderSolutionRequest) {
    }
}
