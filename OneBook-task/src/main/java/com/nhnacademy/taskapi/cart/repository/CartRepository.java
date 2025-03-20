package com.nhnacademy.taskapi.cart.repository;

import com.nhnacademy.taskapi.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findCartByMemberId(Long memberId);
}
