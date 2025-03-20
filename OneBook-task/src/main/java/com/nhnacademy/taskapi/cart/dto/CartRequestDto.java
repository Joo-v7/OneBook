package com.nhnacademy.taskapi.cart.dto;

import java.util.List;

public record CartRequestDto(
        List<CartItemRequestDto> cartItemRequestDtoList
) { }

