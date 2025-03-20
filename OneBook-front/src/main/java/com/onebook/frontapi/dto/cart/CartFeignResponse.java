package com.onebook.frontapi.dto.cart;

import java.util.List;

public record CartFeignResponse(
        String cartId,
        Long memberId,
        List<CartItemFeignResponse> cartItemFeignResponseList
){}