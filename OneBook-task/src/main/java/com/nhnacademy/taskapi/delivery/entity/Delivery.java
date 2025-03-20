package com.nhnacademy.taskapi.delivery.entity;

import com.nhnacademy.taskapi.delivery.domain.Status;
import com.nhnacademy.taskapi.order.entity.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    Long deliveryId;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    Order order;

    LocalDateTime startDate;
    LocalDateTime doneDate;
    LocalDate expectedDate;
    String recipientName;
    String recipientPhoneNumber;
    String recipientAddress;
    String recipientRequestedTerm;
    Status status;

    // 송장번호
    String invoiceNumber;

    public Delivery(Order order, String recipientName, String recipientPhoneNumber, String recipientAddress, String recipientRequestedTerm, LocalDate expectedDate) {
        this.order = order;
        this.recipientName = recipientName;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.recipientAddress = recipientAddress;
        this.recipientRequestedTerm = recipientRequestedTerm;
        this.expectedDate = expectedDate;
        this.status = Status.배송전;
    }
}
