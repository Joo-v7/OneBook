package com.nhnacademy.taskapi.order.dto;

import com.nhnacademy.taskapi.order.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderDetailBookResponse {
    private Long orderDetailId;
    private Long bookId;
    private String bookTitle;
    private int quantity;
    private int price;
    private int discountAmount;

    public static OrderDetailBookResponse fromOrderDetail(OrderDetail orderDetail) {
        return new OrderDetailBookResponse(
                orderDetail.getOrderDetailId(),
                orderDetail.getBook().getBookId(),
                orderDetail.getBook().getTitle(),
                orderDetail.getQuantity(),
                orderDetail.getBookPrice(),
                orderDetail.getDiscountAmount()
        );
    }
}
