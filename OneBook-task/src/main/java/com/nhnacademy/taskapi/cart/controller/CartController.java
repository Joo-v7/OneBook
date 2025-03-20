package com.nhnacademy.taskapi.cart.controller;

import com.nhnacademy.taskapi.cart.dto.CartRequestDto;
import com.nhnacademy.taskapi.cart.dto.CartResponseDto;
import com.nhnacademy.taskapi.cart.exception.CartIllegalArgumentException;
import com.nhnacademy.taskapi.cart.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/task/carts")
@Tag(name = "Cart", description = "장바구니 생성, 조회, 등록, 수정")  // API 그룹 설명 추가
public class CartController {

    private final CartService cartService;

    // 장바구니 조회 by memberId
    @GetMapping
    public ResponseEntity<CartResponseDto> getCartForMemberById(
            @RequestHeader(value="X-MEMBER-ID", required = false) Long memberId) {
        if(Objects.isNull(memberId)) {
            throw new CartIllegalArgumentException("memberId is null");
        }
        CartResponseDto result = cartService.getCartByMemberId(memberId);
        return ResponseEntity.ok().body(result);
    }

    // 장바구니 조회 by loginId
    @GetMapping("/member")
    public ResponseEntity<CartResponseDto> getCartForMemberByLoginId(@RequestBody String loginId) {
        CartResponseDto result = cartService.getCartByLoginId(loginId);
        return ResponseEntity.ok().body(result);
    }

    // 장바구니 단일 조회.
    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDto> getCart(@PathVariable String id) {
        CartResponseDto result = cartService.getCartById(id);
        return ResponseEntity.ok().body(result);
    }

    // 장바구니 저장.
    @PostMapping("/{id}")
    public ResponseEntity<CartResponseDto> registerCart(
            @PathVariable String id,
            @RequestHeader(value = "X-MEMBER-ID", required = false) Long memberId, // 없으면 null 로 받음.
            @RequestBody CartRequestDto cartRequestDto) {

        CartResponseDto result = null;
        if(memberId == null) {
            // 비회원
            result = cartService.registerNonMemberCart(id, cartRequestDto);
        }else {
            // 회원
            result = cartService.registerMemberCart(id, memberId, cartRequestDto);
        }
        return ResponseEntity.ok().body(result);
    }

    // 장바구니 수정.
    @PutMapping("/{id}")
    public ResponseEntity<CartResponseDto> modifyCart(
            @PathVariable String id,
            @RequestBody CartRequestDto cartRequestDto) {

        CartResponseDto cartResponseDto = cartService.modifyCart(id, cartRequestDto);
        return ResponseEntity.ok().body(cartResponseDto);
    }

    // 장바구니 삭제.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeCart(@PathVariable String id) {
        cartService.removeCart(id);
        return ResponseEntity.noContent().build();
    }

}
