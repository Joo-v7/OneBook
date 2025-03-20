package com.nhnacademy.taskapi.payment.repository;

import com.nhnacademy.taskapi.payment.domain.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {
}
