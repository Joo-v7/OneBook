package com.nhnacademy.taskapi.cart.domain;

import com.nhnacademy.taskapi.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
@Entity
public class Cart {

    @Id
    @Column(name="cart_id")
    private String id;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @Setter
    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    // 비회원 장바구니.
    public Cart(String id) {
        this.id = id;
        this.member = null;
    }

    // 회원 장바구니.
    public Cart(String id ,Member member) {
        this.id = id;
        this.member = member;
    }

    public Cart setMember(Member member) {
        this.member = member;
        return this;
    }

}
