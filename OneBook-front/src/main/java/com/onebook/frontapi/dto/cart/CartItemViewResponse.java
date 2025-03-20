package com.onebook.frontapi.dto.cart;

import com.onebook.frontapi.dto.book.BookDTO;

public record CartItemViewResponse(
        Long bookId,
        String title,
        int price,
        int salePrice,
        String image,
        int quantity,
        int stock
) {
}
