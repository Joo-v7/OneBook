package com.nhnacademy.taskapi.member.repository;

import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.domain.QMember;
import com.nhnacademy.taskapi.order.entity.QOrder;
import com.nhnacademy.taskapi.order.entity.QOrderStatus;
import com.nhnacademy.taskapi.payment.domain.QPayment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 2025/1/17(금)
     * 김주혁
     * memberId로 멤버의 모든 Payment 조회
     */
    public List<Integer> getMemberNetPaymentAmounts(Long memberId) {
        QPayment payment = QPayment.payment;
        QOrder order = QOrder.order;
        QOrderStatus orderStatus = QOrderStatus.orderStatus;

        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

        return jpaQueryFactory.select(payment.onlyBookAmount)
                .from(payment)
                .join(order).on(payment.order.orderId.eq(order.orderId)) // payments와 orders 조인
                .where(order.member.id.eq(memberId) // member_id 조건
                        .and(orderStatus.statusName.eq("구매확정")) // order_status_name 조건
                        .and(order.dateTime.goe(threeMonthsAgo))) // 최근 3개월 조건
                .fetch(); // onlyBookAmount 컬럼 값 리스트로 반환
    }

    // 최근 로그인한 날짜를 기준으로 3달 이전 멤버 불러오기. (lastLoginAt이 null 인 멤버는 적용 X)
    public List<Member> getAllMemberByBatch() {
        QMember member = QMember.member;
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        log.info("threeMonthsAgo:{}", threeMonthsAgo.toString());
        return jpaQueryFactory.select(member)
                .from(member)
                .where(member.lastLoginAt.lt(threeMonthsAgo)
                        .and(member.lastLoginAt.isNotNull())) // 현재 시점에서 3개월 이전
                .fetch();
    }

}
