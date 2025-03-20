package com.nhnacademy.taskapi.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "order_statuses")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_status_id")
    private int orderStatusId;

    @Column(name = "order_status_name", unique = true)
    private String statusName;

    public OrderStatus(String statusName) {
        this.statusName = statusName;
    }
}
