package com.nhnacademy.taskapi.order.repository;

import com.nhnacademy.taskapi.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
