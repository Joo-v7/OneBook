package com.nhnacademy.taskapi.cart.dto;

import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(
        Long bookId,
        int quantity
) { }
