package com.nhnacademy.taskapi.delivery.repository;

import com.nhnacademy.taskapi.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

}
