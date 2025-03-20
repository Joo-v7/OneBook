package com.onebook.frontapi.controller.order;

import com.onebook.frontapi.dto.order.OrderCouponResponse;
import com.onebook.frontapi.service.order.OrderCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class OrderCouponController {

    private final OrderCouponService orderCouponService;

    @GetMapping("/order/coupon")
    public String getCouponsByBookId(@RequestParam("book-id") Long bookId, Model model) {
        List<OrderCouponResponse> coupons = orderCouponService.getCoupon(bookId);

        model.addAttribute("coupons", coupons);
        return "order/order-coupon"; // 해당 쿠폰 리스트를 보여줄 HTML 파일
    }
}
