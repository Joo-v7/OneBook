package com.onebook.frontapi.controller.payment;

import com.onebook.frontapi.dto.order.OrderDetailBookFeignResponse;
import com.onebook.frontapi.dto.order.OrderDetailResponse;
import com.onebook.frontapi.service.cart.CartService;
import com.onebook.frontapi.service.order.OrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

import static com.onebook.frontapi.service.cart.CartService.CART_ID;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PaymentResultController {

    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam String amount,
            Model model,
            HttpServletRequest request
    ) {

        // 필요한 로직 or DB 업데이트 / 보여줄 모델 데이터
        model.addAttribute("paymentKey", paymentKey);
        model.addAttribute("orderId", orderId);
        model.addAttribute("amount", amount);

        /**
         * 결제 성공시 장바구니에서 아이템 삭제.
         */

        // 장바구니 쿠키 가져오기.
        Cookie[] cookies = request.getCookies();
        if(Objects.isNull(cookies)) {
            return "payment/success";
        }

        // orderId 파싱
        String[] orderIds  = orderId.split("_");
        String realOrderId = orderIds[0];

        try {
            for (Cookie c : cookies) {
                if (c.getName().equals(CART_ID)) { // 쿠키에서 cartId 가져오기
                    String cartId = c.getValue();
                    // orderDetails 가져오기
                    OrderDetailResponse orderDetail = orderService.getOrderDetail(Long.parseLong(realOrderId));
                    List<OrderDetailBookFeignResponse> items = orderDetail.getItems();

                    if (Objects.isNull(items) || items.isEmpty()) {
                        return "payment/success";
                    }

                    // 장바구니에서 item 삭제.
                    for (OrderDetailBookFeignResponse o : items) {
                        // 장바구니(redis)에서 아이템 삭제.
                        cartService.removeCartItem(cartId, o.getBookId());
                    }
                }
            }
        }catch(Exception e) {
            log.info("결제 성공시 장바구니에서 아이템 삭제 과정에서 오류 발생", e);
            return "payment/success";
        }

        return "payment/success";
    }

    @GetMapping("/fail")
    public String paymentFail(
            @RequestParam(required=false) String code,
            @RequestParam(required=false) String message,
            Model model
    ) {
        model.addAttribute("code", code);
        model.addAttribute("message", message);
        return "payment/fail";
    }
}
