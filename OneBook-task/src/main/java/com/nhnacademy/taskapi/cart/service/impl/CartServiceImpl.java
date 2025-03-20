package com.nhnacademy.taskapi.cart.service.impl;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.cart.domain.Cart;
import com.nhnacademy.taskapi.cart.domain.CartItem;
import com.nhnacademy.taskapi.cart.dto.CartItemRequestDto;
import com.nhnacademy.taskapi.cart.dto.CartRequestDto;
import com.nhnacademy.taskapi.cart.dto.CartResponseDto;
import com.nhnacademy.taskapi.cart.exception.CartIllegalArgumentException;
import com.nhnacademy.taskapi.cart.exception.CartNotFoundException;
import com.nhnacademy.taskapi.cart.repository.CartRepository;
import com.nhnacademy.taskapi.cart.service.CartService;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.exception.MemberNotFoundException;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    // 장바구니 조회 by cartId
    @Transactional(readOnly = true)
    @Override
    public CartResponseDto getCartById(String cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException("Cart Not Found by " + cartId));
        return CartResponseDto.from(cart);
    }

    // 장바구니 조회 by memberId
    @Transactional(readOnly = true)
    @Override
    public CartResponseDto getCartByMemberId(Long memberId) {
        Cart cart = cartRepository.findCartByMemberId(memberId).orElseThrow(()-> new CartNotFoundException("Cart Not Found by " + memberId));
        return CartResponseDto.from(cart);
    }

    // 장바구니 조회 by loginId
    @Transactional(readOnly = true)
    @Override
    public CartResponseDto getCartByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(()-> new MemberNotFoundException("Member Not Found by " + loginId));
        Cart cart = cartRepository.findCartByMemberId(member.getId()).orElseThrow(()-> new CartNotFoundException("Cart Not Found by " + member.getId()));
        return CartResponseDto.from((cart));
    }

    // 비회원 장바구니 저장.
    @Override
    public CartResponseDto registerNonMemberCart(String cartId, CartRequestDto cartRequestDto) {
        // cart 생성/저장
        Cart cart = cartRepository.save(new Cart(cartId));

        // List<CartItemRequestDto> -> List<CartItem>
        List<CartItem> cartItems = toCartItems(cart, cartRequestDto);

        // Cart에 CartItems setter, 저장(트랜잭션 열려있으므로 저장됨).
        cart.setCartItems(cartItems);

        return CartResponseDto.from(cart);
    }

    // 회원 장바구니 저장.
    @Override
    public CartResponseDto registerMemberCart(String cartId, Long memberId, CartRequestDto cartRequestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new MemberNotFoundException("Member Not Found by " + memberId));

        Cart cart = cartRepository.save(new Cart(cartId, member));

        // List<CartItemRequestDto> -> List<CartItem>
        List<CartItem> cartItems = toCartItems(cart, cartRequestDto);

        // Cart에 CartItems setter, 저장(트랜잭션 열려있으므로 저장됨).
        cart.setCartItems(cartItems);

        return CartResponseDto.from(cart);
    }

    // 장바구니 수정 - cartItems update
    @Override
    public CartResponseDto modifyCart(String cartId, CartRequestDto cartRequestDto) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException("Cart Not Found by " + cartId));

        List<CartItem> cartItems = toCartItems(cart, cartRequestDto);

        cart.setCartItems(cartItems);

        return CartResponseDto.from(cart);
    }

    // 장바구니 삭제
    @Override
    public void removeCart (String cartId){
        if (!cartRepository.existsById(cartId)) {
            throw new CartIllegalArgumentException("Cart ID doesn't exist");
        }
        cartRepository.deleteById(cartId);
    }

    private List<CartItem> toCartItems(Cart cart, CartRequestDto cartRequestDto) {
        // List<CartItemRequestDto> -> List<CartItem>
        List<CartItem> cartItems = new ArrayList<>();
        if (cartRequestDto != null && cartRequestDto.cartItemRequestDtoList() != null) {
            for (CartItemRequestDto cir : cartRequestDto.cartItemRequestDtoList()) {
                if (Objects.nonNull(cir)) {
                    Book book = bookRepository.findById(cir.bookId()).orElse(null);
                    CartItem cartItem = new CartItem(cart, book, cir.quantity());
                    cartItems.add(cartItem);
                }
            }
        }
        return cartItems;
    }

}
