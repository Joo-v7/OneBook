package com.nhnacademy.taskapi.order.repository;

import com.nhnacademy.taskapi.order.entity.Order;
import com.nhnacademy.taskapi.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberId(Long memberId);
    Page<Order> findByMemberId(Long memberId, Pageable pageable);

    List<Order> findByOrderStatus(OrderStatus orderStatus);

    Page<Order> findByMemberIdAndOrderStatus(Long memberId, OrderStatus orderStatus, Pageable pageable);

    Page<Order> findByMemberIdAndOrderStatusNot(Long memberId, OrderStatus orderStatus, Pageable pageable);

    Page<Order> findByMemberIdAndOrderStatusStatusNameOrOrderStatusIsNull(Long memberId, OrderStatus orderStatus, Pageable pageable);

//     TODO:QueryFactory 찾아보기
//    @Query("SELECT o FROM Order o JOIN FETCH o.orderStatus WHERE o.orderStatus.statusName = :statusName") // 캐시 사용불가
//    List<Order> findByStatusName(@Param("statusName") String statusName);
}
