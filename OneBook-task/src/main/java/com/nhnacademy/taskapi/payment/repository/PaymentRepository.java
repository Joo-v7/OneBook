package com.nhnacademy.taskapi.payment.repository;

import com.nhnacademy.taskapi.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // orderId로 Payment 조회 (토스 승인 시 필요)
    Payment findByOrder_OrderId(Long orderId);

    // 특정회원의 결제가 완료된 주문을 찾아야함
    List<Payment> findByOrder_Member_IdAndStatus(Long memberId, String status);
}
