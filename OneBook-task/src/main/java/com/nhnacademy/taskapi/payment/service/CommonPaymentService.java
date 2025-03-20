package com.nhnacademy.taskapi.payment.service;

import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.order.entity.Order;
import com.nhnacademy.taskapi.payment.domain.Payment;
import com.nhnacademy.taskapi.payment.domain.PaymentMethod;
import com.nhnacademy.taskapi.payment.exception.InsufficientPointException;
import com.nhnacademy.taskapi.payment.exception.InvalidPaymentException;
import com.nhnacademy.taskapi.payment.exception.PaymentNotFoundException;
import com.nhnacademy.taskapi.point.domain.Point;
import com.nhnacademy.taskapi.point.jpa.JpaPointRepository;
import com.nhnacademy.taskapi.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 포인트 차감, 적립 등 공통 로직 관리
 * 다른 PaymentServiceImpl, TossPaymentServiceImpl 등에서 주입받아 사용
 */
@Service
@RequiredArgsConstructor
public class CommonPaymentService {
    private final JpaPointRepository pointRepository;
    private final PointService pointService;
    private final MemberRepository memberRepository;

    /**
     * 전액 포인트 결제 시 처리
     * 1) 회원 포인트 충분한지 체크
     * 2) 포인트 차감
     * 3) PaymentMethod (POINT) 업데이트
     * 4) Payment 상태를 DONE으로 세팅
     */
    public void handleFullPointPayment(Payment payment, Long memberId) {
        // 1) 회원 포인트 충분한지 체크
        Point userPoint = pointRepository.findByMember_Id(memberId)
                .orElseThrow(() -> new InvalidPaymentException("포인트 정보가 없습니다."));
        int usedPoint = payment.getPoint();
        if (userPoint.getAmount() < usedPoint) {
            throw new InsufficientPointException("보유 포인트가 부족하여 전액 포인트 결제를 진행할 수 없습니다.");
        }

        // 2) 포인트 차감
        pointService.usePointsForPayment(memberId, usedPoint);

        // 3) PaymentMethod를 "POINT" 방식으로 설정
        if (payment.getPaymentMethod() == null) {
            PaymentMethod pm = new PaymentMethod();
            payment.setPaymentMethod(pm);
        }

        payment.getPaymentMethod().setType("POINT");
        payment.getPaymentMethod().setMethod("POINT");

        // paymentKey 예) "POINT_20250108_1610_abcdef"
        String randomSuffix = generateRandomString(6);
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String pointPaymentKey = "POINT_" + formattedDate + "_" + randomSuffix;
        payment.getPaymentMethod().setPaymentKey(pointPaymentKey);

        // 4) Payment이 즉시 결제완료가 되므로 approvedAt도 지금 시각
        payment.setApprovedAt(LocalDateTime.now());
        payment.setStatus("DONE");
    }

    // 랜덤 문자열 생성 유틸
    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for(int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 책 구매 시 포인트 적립 (순수 도서금액이 양수일 때만)
     */
    public void accumulationPurchasePoints(Payment payment) {
        int usedPoint = payment.getPoint();
        int onlyBookAmount = payment.getOrder().getTotalPrice() - usedPoint;

        // order.getTotalPrice()는 배송비, 포장비 제외한 순수 도서가격이라고 가정
        if (onlyBookAmount > 0) {
            Long memberId = payment.getOrder().getMember().getId();
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new InvalidPaymentException("회원을 찾을 수 없습니다."));
            pointService.registerPurchasePoints(member, onlyBookAmount);
        }
    }

    // 포인트 차감
    public void usedPurchasePoint(Payment payment) {
        // 1) 사용된 포인트 확인
        int usedPoint = payment.getPoint();
        if (usedPoint > 0) {
            Long memberId = payment.getOrder().getMember().getId();
            // 포인트 부족 체크
            Point userPoint = pointRepository.findByMember_Id(memberId)
                    .orElseThrow(() -> new PaymentNotFoundException("회원 포인트를 찾을 수 없습니다."));
            if (userPoint.getAmount() < usedPoint) {
                throw new InsufficientPointException("포인트가 부족합니다.");
            }
            // 포인트 차감
            pointService.usePointsForPayment(memberId, usedPoint);
        }
    }
}
