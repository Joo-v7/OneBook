package com.nhnacademy.taskapi.order.entity;

import com.nhnacademy.taskapi.book.domain.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    Long orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne
    @JoinColumn(name = "book_id")
    Book book;
    int bookPrice; // 1권의 책가격
    int quantity; // 책 수량
    int discountAmount; // 쿠폰으로 할인받은 가격
//    int bookTotalPrice; // 쿠폰이 적용된 실제 책 가격(책 가격 * 수량 - 쿠폰할인가격)

    public OrderDetail(Order order, Book book, int bookPrice, int quantity, int discountAmount, int bookTotalPrice) {
        this.order = order;
        this.book = book;
        this.bookPrice = bookPrice;
        this.quantity = quantity;
        this.discountAmount = discountAmount;
    }
}
