package com.onebook.frontapi.dto.cart;

public record CartRequest(
        Long bookId,
        int quantity
) {
}
