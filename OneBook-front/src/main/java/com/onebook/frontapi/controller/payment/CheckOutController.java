package com.onebook.frontapi.controller.payment;

import com.onebook.frontapi.feign.member.MemberClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@RequiredArgsConstructor
@Controller
public class CheckOutController {

    private final MemberClient memberClient;

    @GetMapping("/front/payments/checkout-page")
    public String showCheckout(@RequestParam String orderId, Model model) {
        return "payment/checkout";
    }
}

