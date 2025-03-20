package com.nhnacademy.taskapi.cart.dto;

import com.nhnacademy.taskapi.cart.domain.Cart;
import com.nhnacademy.taskapi.cart.domain.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record CartResponseDto(
        String cartId,
        Long memberId,
        List<CartItemResponseDto> cartItemResponseDtoList
)

{
    // Cart -> CartResponseDto
    public static CartResponseDto from(Cart cart) {
        List<CartItemResponseDto> cartItemResponseDtos = new ArrayList<>();

        if(Objects.nonNull(cart.getCartItems())) {
            cartItemResponseDtos = convert(cart.getCartItems());
        }

        if(Objects.nonNull(cart.getMember())) {
            return new CartResponseDto(
                    cart.getId(),
                    cart.getMember().getId(),
                    cartItemResponseDtos
            );
        }
        return new CartResponseDto(cart.getId(), null, cartItemResponseDtos);
    }

    // List<CartItem> -> List<CartItemResponseDto>
    private static List<CartItemResponseDto> convert(List<CartItem> cartItems) {
        if(Objects.isNull(cartItems)) {
            return null;
        }

        List<CartItemResponseDto> cartItemResponseDtoList = new ArrayList<>();
        for(CartItem ci : cartItems) {
            if(Objects.nonNull(ci)) {
                CartItemResponseDto cartItemResponseDto = new CartItemResponseDto(ci.getBook().getBookId(), ci.getQuantity());
                cartItemResponseDtoList.add(cartItemResponseDto);
            }
        }
        return cartItemResponseDtoList;
    }
}
