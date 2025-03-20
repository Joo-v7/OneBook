package com.onebook.frontapi.controller.order;

import com.onebook.frontapi.dto.order.*;
import com.onebook.frontapi.service.image.ImageService;
import com.onebook.frontapi.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/my/order/return")
@Controller
public class OrderSolutionController {

    private final OrderService orderService;
    private final ImageService imageService;

    @GetMapping("/{order-id}/step1")
    public String ReturnStepOne(@PathVariable("order-id") Long orderId, Model model) {
        model.addAttribute("orderId", orderId);

        return "order/exchange-return-step1";
    }

    @PostMapping("/{order-id}/step1")
    public String ReturnStepOne(@PathVariable("order-id") Long orderId, @RequestParam("solutionType") String solutionType,
                                HttpSession session, Model model) {
        // session 저장
        session.setAttribute(orderId.toString(), solutionType);

        OrderDetailResponse orderDetail = orderService.getOrderDetail(orderId);

        // 이미지 불러오기
        // TODO 변경 필요
        List<String> imgList = new ArrayList<>();
        for (OrderDetailBookFeignResponse item : orderDetail.getItems()) {
            imgList.add(imageService.getImage(item.getBookId()).getUrl());
        }

        model.addAttribute("orderId", orderId);
        model.addAttribute("orderItems", orderDetail.getItems());
        model.addAttribute("itemImages", imgList);

        return "order/exchange-return-step2";
    }

    @PostMapping("/{order-id}/step2")
    public String processStep2(@PathVariable("order-id") Long orderId, @RequestBody BookListRequest bookListRequest,
            HttpSession session, Model model) {

        // 정보 세션 저장
        String solutionType = (String) session.getAttribute(orderId.toString());
        OrderSolutionRequest orderSolutionRequest = new OrderSolutionRequest(solutionType, bookListRequest.getBookOrderRequests());
        session.setAttribute(orderId.toString(), orderSolutionRequest);

        model.addAttribute("orderId", orderId);
        model.addAttribute("solutionType", solutionType);

        return "redirect:/my/order/return/" + orderId + "/step3";
    }

    // FetchAPI 방식으로 무조건 redirect 로 받아야함
    @GetMapping("/{order-id}/step3")
    public String ReturnStep3(@PathVariable("order-id") Long orderId, HttpSession session, Model model) {
        OrderSolutionRequest orderSolutionRequest = (OrderSolutionRequest) session.getAttribute(orderId.toString());
        if (orderSolutionRequest == null) {
            // 세션이 만료되었거나 잘못된 접근일 경우 처리
            return "redirect:/my/order/return/" + orderId + "/step1";
        }

        model.addAttribute("orderId", orderId);
        model.addAttribute("solutionType", orderSolutionRequest.getSolutionType());
        return "order/exchange-return-step3";
    }

    @PostMapping("/{order-id}/submit")
    public String ReturnStepThree(@PathVariable("order-id") Long orderId, @RequestParam String reason, HttpSession session) {
        OrderSolutionRequest orderSolutionRequest = (OrderSolutionRequest) session.getAttribute(orderId.toString());
        orderSolutionRequest.setReason(reason);

        // DB 저장 할 service 호출
        orderService.createOrderSolution(orderId, orderSolutionRequest);

        // 상태변경
        // TODO dto 한번에 통신 가능?
        List<Long> orderIds = new ArrayList<>();
        orderIds.add(orderId);
        orderService.updateOrderStatus(orderIds, orderSolutionRequest.getSolutionType().equals("RETURN") ? "반품" : "교환");

        return "redirect:/my/orders";
    }
}
