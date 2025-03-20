package com.onebook.frontapi.dto.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record CartItemResponse(
        Long bookId,
        int quantity
) {
    // CartItemFeignResponse -> CartItemResponse
    public static CartItemResponse fromCartItemResponseDto(CartItemFeignResponse cartItemFeignResponse) {
       return new CartItemResponse(
               cartItemFeignResponse.bookId(),
               cartItemFeignResponse.quantity()
       );
    }

    // Map<Object, Object> -> CartItemResponse
    public static List<CartItemResponse> fromMapToList(Map<Object, Object> map) {
        List<CartItemResponse> cartItemResponses = new ArrayList<>();

        if(Objects.isNull(map) || map.isEmpty()) {
            return cartItemResponses;
        }

        for(Map.Entry<Object, Object> entry : map.entrySet()) {
            Long bookId = Long.parseLong(entry.getKey().toString());
            int quantity = Integer.parseInt(entry.getValue().toString());
            CartItemResponse cartItemResponse = new CartItemResponse(bookId, quantity);
            cartItemResponses.add(cartItemResponse);
        }

        return cartItemResponses;
    }
}
