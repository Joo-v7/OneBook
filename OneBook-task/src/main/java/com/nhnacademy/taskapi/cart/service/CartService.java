package com.nhnacademy.taskapi.cart.service;

import com.nhnacademy.taskapi.cart.dto.CartRequestDto;
import com.nhnacademy.taskapi.cart.dto.CartResponseDto;

public interface CartService {
    CartResponseDto getCartById(String cartId);
    CartResponseDto getCartByMemberId(Long memberId);
    CartResponseDto getCartByLoginId(String loginId);
    CartResponseDto registerNonMemberCart(String cartId, CartRequestDto cartRequestDto);
    CartResponseDto registerMemberCart(String cartId, Long memberId, CartRequestDto cartRequestDto);
    CartResponseDto modifyCart(String cartId, CartRequestDto cartRequestDto);
    void removeCart(String cartId);
}
