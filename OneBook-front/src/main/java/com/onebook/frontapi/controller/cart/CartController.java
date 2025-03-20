package com.onebook.frontapi.controller.cart;

import com.onebook.frontapi.dto.cart.CartRequest;
import com.onebook.frontapi.dto.cart.CartItemViewResponse;
import com.onebook.frontapi.service.cart.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    // 장바구니 목록 페이지
    @GetMapping
    public String cartForm(HttpServletRequest request,
                           HttpServletResponse response,
                           Model model) throws IOException {

        String cartId = cartService.getCartIdFromCookie(request, response);
        List<CartItemViewResponse> cartItemViews =cartService.getCartItemsFromRedisById(cartId);
        model.addAttribute("cartItems", cartItemViews);

        return "cart/cartForm";
    }

    // 장바구니에 도서 담기 -> true, false로 리턴해야 자바스크립트에서 받고 "장바구니로 이동하기" 팝업을 보여준다.
    @PostMapping
    public ResponseEntity<Boolean> addItemToCart(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @ModelAttribute CartRequest cartRequest) throws IOException {

        String cartId = cartService.getCartIdFromCookie(request, response);
        boolean result = cartService.addCartItem(cartId, cartRequest);

        return ResponseEntity.ok(result);
    }

    // 장바구니에서 도서 삭제
    @DeleteMapping
    public String deleteCartItem(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestParam Long bookId) throws IOException {

        String cartId = cartService.getCartIdFromCookie(request, response);
        cartService.removeCartItem(cartId, bookId);

        return "redirect:/cart";
    }

    // 장바구니 도서 수량 변경
    @PutMapping
    public String updateCartItem(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @ModelAttribute CartRequest cartRequest) throws IOException {

        String cartId = cartService.getCartIdFromCookie(request, response);
        cartService.modifyCartItem(cartId, cartRequest);

        return "redirect:/cart";
    }

}
