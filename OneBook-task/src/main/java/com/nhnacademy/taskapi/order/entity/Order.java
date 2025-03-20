package com.nhnacademy.taskapi.order.entity;

import com.nhnacademy.taskapi.delivery.entity.Delivery;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.packaging.entity.Packaging;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
@Entity
public class Order {
    @Id
    @Tsid
    @Column(name = "order_id")
    Long orderId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;

    @Column(name = "order_name")
    String orderName;
    @Column(name = "orderer_name")
    String ordererName;
    @Column(name = "orderer_phone_number")
    String ordererPhoneNumber;
    @Column(name = "date_time")
    LocalDateTime dateTime;
    @Column(name = "delivery_price")
    int deliveryPrice;
    @Column(name = "total_price")
    int totalPrice;

    // 양방향
    @OneToMany(mappedBy = "order")
    List<OrderDetail> orderDetailList;

    @OneToMany(mappedBy = "order")
    List<Delivery> deliveryList;

    @ManyToOne
    @JoinColumn(name = "order_status_id")
    OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "packaging_id")
    Packaging packaging;

    @Column(name = "packaging_price")
    int packagingPrice;

    public Order(Member member, String ordererName, String ordererPhoneNumber, LocalDateTime dateTime, int deliveryPrice, int totalPrice) {
        this.member = member;
        this.ordererName = ordererName;
        this.ordererPhoneNumber = ordererPhoneNumber;
        this.dateTime = dateTime;
        this.deliveryPrice = deliveryPrice;
        this.totalPrice = totalPrice;
    }

    public Order(Member member, String ordererName, String ordererPhoneNumber, LocalDateTime dateTime, int deliveryPrice, int totalPrice, String orderName, Packaging packaging, int packagingPrice, OrderStatus orderStatus) {
        this.member = member;
        this.ordererName = ordererName;
        this.ordererPhoneNumber = ordererPhoneNumber;
        this.dateTime = dateTime;
        this.deliveryPrice = deliveryPrice;
        this.totalPrice = totalPrice;
        this.orderName = orderName;
        this.packaging = packaging;
        this.packagingPrice = packagingPrice;
        this.orderStatus = orderStatus;
    }
}