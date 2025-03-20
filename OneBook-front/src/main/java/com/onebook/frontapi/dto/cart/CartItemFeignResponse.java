package com.onebook.frontapi.dto.cart;

public record CartItemFeignResponse(
        Long bookId,
        int quantity
) { }