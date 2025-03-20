package com.onebook.frontapi.feign.cart;

import com.onebook.frontapi.dto.cart.CartFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="cartClient", url="${onebook.gatewayUrl}")
public interface CartClient {
    // cartId로 cart 가져오기.
    @GetMapping("/task/carts/{id}")
    CartFeignResponse getRequestById(@PathVariable("id") String id);

    // loginId로 cart 가져오기
    @GetMapping("/task/carts/member")
    CartFeignResponse getRequestByLoginId(@RequestParam String loginId);

}
