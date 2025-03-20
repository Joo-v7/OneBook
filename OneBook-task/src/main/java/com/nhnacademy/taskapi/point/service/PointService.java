package com.nhnacademy.taskapi.point.service;

import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.point.domain.Point;
import com.nhnacademy.taskapi.point.domain.PointLog;
import com.nhnacademy.taskapi.point.domain.PointLogUpdatedType;
import com.nhnacademy.taskapi.point.domain.PointPolicy;
import com.nhnacademy.taskapi.point.dto.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PointService {

    // 회원가입 시 포인트 적립 (기본 5000포인트)
    void registerMemberPoints(Member member);

    // 리뷰 작성 시 포인트 적립 (기본 200포인트, 사진 첨부 시 500포인트)
    void registerReviewPoints(Member member, boolean isPhotoAttached);

    // 도서 결제 시 포인트 적립 (등급별 적립률 적용)
    void registerPurchasePoints(Member member, int purchaseAmount);

    // 포인트 정책 수정
    void updatePointPolicy(Long pointPolicyId, PointPolicy pointPolicy);

    // 포인트 결제 기능
    void usePointsForPayment(Long member_id, int paymentAmount);

    // 포인트 환불 기능
    void updatePointByRefund(Long member_id, int refundAmount);

    // 회원의 포인트 정보를 반환
    Point getMemberPoints(Long member_id);

    // 특정 회원의 포인트 로그를 페이징하여 가져옵니다.
    List<PointLog> getPointLogsByMember(Long member_id);

    // 특정 회원의 포인트 로그를 페이징하여 가져옵니다.
    List<PointLog> getPointLogsByMember(Long member_id, Pageable pageable);

    // 활성화된 포인트 정책을 가져옵니다. (페이지네이션)
    List<PointPolicy> getActivePointPolicies(Pageable pageable);

    // 활성화된 포인트 정책을 가져옵니다. (전체 리스트)
    List<PointPolicy> getActivePointPolicies();

    // 포인트 추가 기능
    ApiResponse<String> addPoints(Long memberId, int amount, PointLogUpdatedType updatedTypeEnum);
}



//사진 첨부된 경우 500포인트 적립
//pointService.registerReviewPoints(member, true);
//사진 첨부되지 않은 경우 200포인트 적립
//pointService.registerReviewPoints(member, false);