package com.nhnacademy.taskapi.cart.repository;

import com.nhnacademy.taskapi.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT cb FROM CartItem cb WHERE cb.cart.id = :cartId")
    List<CartItem> findCartBooksByCartId(@Param("cartId") String cartId);
}
