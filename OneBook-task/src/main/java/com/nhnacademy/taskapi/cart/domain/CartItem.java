package com.nhnacademy.taskapi.cart.domain;

import com.nhnacademy.taskapi.book.domain.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Table(name = "cart_items")
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Setter
    @ManyToOne
    @JoinColumn(name="book_id", nullable = false)
    private Book book;

    @Setter
    @Column(nullable = false)
    private int quantity;

    public CartItem(Cart cart, Book book, int quantity) {
        this.cart=cart;
        this.book=book;
        this.quantity=quantity;
    }
}
