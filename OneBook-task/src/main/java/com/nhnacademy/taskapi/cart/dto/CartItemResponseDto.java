package com.nhnacademy.taskapi.cart.dto;

public record CartItemResponseDto(
        Long bookId,
        int quantity
) { }
