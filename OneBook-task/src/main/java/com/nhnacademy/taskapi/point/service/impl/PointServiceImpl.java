package com.nhnacademy.taskapi.point.service.impl;

import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.point.domain.Point;
import com.nhnacademy.taskapi.point.domain.PointLog;
import com.nhnacademy.taskapi.point.domain.PointLogUpdatedType;
import com.nhnacademy.taskapi.point.domain.PointPolicy;
import com.nhnacademy.taskapi.point.dto.ApiResponse;
import com.nhnacademy.taskapi.point.exception.PointPolicyException;
import com.nhnacademy.taskapi.point.jpa.JpaPointRepository;
import com.nhnacademy.taskapi.point.jpa.JpaPointPolicyRepository;
import com.nhnacademy.taskapi.point.repository.PointLogRepository;
import com.nhnacademy.taskapi.point.service.PointService;
import com.nhnacademy.taskapi.grade.service.GradeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PointServiceImpl implements PointService {

    private final JpaPointRepository pointRepository;
    private final JpaPointPolicyRepository pointPolicyRepository;
    private final PointLogRepository pointLogRepository;
    private final GradeService gradeService;

    // 생성자 주입
    public PointServiceImpl(JpaPointRepository pointRepository,
                            JpaPointPolicyRepository pointPolicyRepository,
                            PointLogRepository pointLogRepository,
                            GradeService gradeService) {
        this.pointRepository = pointRepository;
        this.pointPolicyRepository = pointPolicyRepository;
        this.pointLogRepository = pointLogRepository;
        this.gradeService = gradeService;
    }

    /**
     * 회원가입 시 포인트 적립 (기본 5000포인트)
     */
    @Override
    public void registerMemberPoints(Member member) {
        Point point = new Point(5000, member);  // 5000포인트 적립
        pointRepository.save(point);

        // 포인트 로그 기록
        PointLog pointLog = PointLog.builder()
                .pointLogUpdatedAt(LocalDateTime.now())
                .pointLogUpdatedType(PointLogUpdatedType.REGISTRATION)  // 회원가입으로 적립
                .pointLogAmount(5000)
                .point(point)
                .afterPointAmount(point.getPointCurrent())
                .build();

        pointLogRepository.save(pointLog);
    }

    /**
     * 리뷰 작성 시 포인트 적립 (기본 200포인트, 사진 첨부 시 500포인트)
     */
    @Override
    public void registerReviewPoints(Member member, boolean isPhotoAttached) {
        Optional<Point> optionalPoint = pointRepository.findByMember_Id(member.getId());
        Point point = optionalPoint.orElseThrow(() -> new PointPolicyException("회원 포인트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // 기본 200포인트 적립, 사진이 첨부되었다면 500포인트 적립
        int pointAmount = isPhotoAttached ? 500 : 200;

        // 포인트 업데이트
        updateMemberPoints(member.getId(), point.getAmount() + pointAmount);

        // 포인트 로그 기록
        PointLog pointLog = PointLog.builder()
                .pointLogUpdatedAt(LocalDateTime.now())
                .pointLogUpdatedType(PointLogUpdatedType.REVIEW)  // 리뷰 작성으로 적립
                .pointLogAmount(pointAmount)
                .point(point)
                .afterPointAmount(point.getPointCurrent())
                .build();

        pointLogRepository.save(pointLog);
    }

    /**
     * 도서 결제 시 포인트 적립 (등급별 적립률 적용)
     * 결제 금액 * 등급에 따른 적립률 계산
     */
    @Override
    public void registerPurchasePoints(Member member, int purchaseAmount) {
        // 회원의 등급 정보를 가져옴
        Grade grade = member.getGrade();  // Member 엔티티에 연결된 Grade 객체를 가져옴
        int accumulationRate = grade.getAccumulationRate();  // 해당 등급의 적립률 가져오기

        // 포인트 적립 계산
        int earnedPoints = calculatePointsByRate(purchaseAmount, accumulationRate);

        Optional<Point> optionalPoint = pointRepository.findByMember_Id(member.getId());
        Point point = optionalPoint.orElseThrow(() -> new PointPolicyException("회원 포인트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // 포인트 업데이트
        updateMemberPoints(member.getId(), point.getAmount() + earnedPoints);

        // 포인트 로그 기록
        PointLog pointLog = PointLog.builder()
                .pointLogUpdatedAt(LocalDateTime.now())
                .pointLogUpdatedType(PointLogUpdatedType.PURCHASE)  // 도서 구매로 적립
                .pointLogAmount(earnedPoints)
                .point(point)
                .afterPointAmount(point.getPointCurrent())
                .build();

        // 포인트 로그 저장
        pointLogRepository.save(pointLog);
    }

    /**
     * 포인트 정책 수정
     */
    @Override
    public void updatePointPolicy(Long pointPolicyId, PointPolicy pointPolicy) {
        // 기존 정책 찾기
        PointPolicy existingPolicy = pointPolicyRepository.findById(pointPolicyId)
                .orElseThrow(() -> new PointPolicyException("포인트 정책을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // 정책 업데이트
        existingPolicy.updatePointPolicyRate(pointPolicy.getPointPolicyRate());
        existingPolicy.updatePointPolicyApplyAmount(pointPolicy.getPointPolicyApplyAmount());

        // 변경 사항 저장
        pointPolicyRepository.save(existingPolicy);
    }

    /**
     * 포인트 결제 기능
     * @param member_id - 회원 ID
     * @param paymentAmount - 결제 금액
     * @throws PointPolicyException - 포인트가 부족한 경우 예외 발생
     */
    @Override
    public void usePointsForPayment(Long member_id, int paymentAmount) {
        // 회원 포인트 조회
        Optional<Point> optionalPoint = pointRepository.findByMember_Id(member_id);
        Point point = optionalPoint.orElseThrow(() -> new PointPolicyException("회원 포인트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // 결제 금액이 포인트보다 많은지 확인
        if (point.getAmount() < paymentAmount) {
            throw new PointPolicyException("포인트가 부족합니다.", HttpStatus.BAD_REQUEST);
        }

        // 포인트 차감
        updateMemberPoints(member_id, point.getAmount() - paymentAmount);

        // 포인트 로그 기록
        PointLog pointLog = PointLog.builder()
                .pointLogUpdatedAt(LocalDateTime.now())
                .pointLogUpdatedType(PointLogUpdatedType.PAYMENT)  // 결제 타입
                .pointLogAmount(-paymentAmount)  // 차감된 포인트는 마이너스
                .point(point)
                .afterPointAmount(point.getPointCurrent())
                .build();

        // 포인트 로그 저장
        pointLogRepository.save(pointLog);
    }

    /**
     * 포인트 환불 기능
     * @param member_id - 회원 ID
     * @param refundAmount - 환불 금액
     * @throws PointPolicyException - 포인트가 부족한 경우 예외 발생
     */
    @Override
    public void updatePointByRefund(Long member_id, int refundAmount) {
        // 회원 포인트 조회
        Optional<Point> optionalPoint = pointRepository.findByMember_Id(member_id);
        Point point = optionalPoint.orElseThrow(() -> new PointPolicyException("회원 포인트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // 환불 금액이 포인트보다 많은지 확인
        if (point.getAmount() < refundAmount) {
            throw new PointPolicyException("환불 금액이 부족합니다.", HttpStatus.BAD_REQUEST);
        }

        // 포인트 복구
        updateMemberPoints(member_id, point.getAmount() + refundAmount);

        // 포인트 로그 기록
        PointLog pointLog = PointLog.builder()
                .pointLogUpdatedAt(LocalDateTime.now())
                .pointLogUpdatedType(PointLogUpdatedType.REFUND)  // 환불 타입
                .pointLogAmount(refundAmount)  // 환불된 포인트
                .point(point)
                .afterPointAmount(point.getPointCurrent())
                .build();

        // 포인트 로그 저장
        pointLogRepository.save(pointLog);
    }

    /**
     * 회원의 포인트 정보를 반환
     */
    @Override
    public Point getMemberPoints(Long member_id) {
        return pointRepository.findByMember_Id(member_id)
                .orElseThrow(() -> new PointPolicyException("회원 포인트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
    }

    /**
     * 특정 회원의 포인트 로그를 페이징하여 가져옵니다.
     */
    @Override
    public List<PointLog> getPointLogsByMember(Long member_id) {
        // 모든 포인트 로그를 반환 (페이징 없는 메서드 사용)
        return pointLogRepository.findByPoint_Member_IdOrderByPointLogUpdatedAtDesc(member_id);
    }

    @Override
    public List<PointLog> getPointLogsByMember(Long member_id, Pageable pageable) {
        // 페이징 처리된 포인트 로그 반환
        Page<PointLog> pointLogPage = pointLogRepository.findByPoint_Member_IdOrderByPointLogUpdatedAtDesc(member_id, pageable);
        return pointLogPage.getContent();  // 페이징된 결과 리스트 반환
    }

    /**
     * 활성화된 포인트 정책을 가져옵니다. (페이지네이션)
     */
    @Override
    public List<PointPolicy> getActivePointPolicies(Pageable pageable) {
        Page<PointPolicy> activePointPolicies = pointPolicyRepository.findAllByPointPolicyStateTrue(pageable); // 'pointPolicyState'가 true인 활성화된 포인트 정책을 페이지네이션을 적용하여 조회
        return activePointPolicies.getContent(); // 페이지네이션 결과에서 콘텐츠만 가져오기
    }

    /**
     * 활성화된 포인트 정책을 가져옵니다. (전체 리스트)
     */
    @Override
    public List<PointPolicy> getActivePointPolicies() {
        // 모든 활성화된 포인트 정책을 조회
        return pointPolicyRepository.findAllByPointPolicyStateTrue();  // 'pointPolicyState'가 true인 정책만 조회
    }

    // 적립률을 기준으로 포인트 계산
    private int calculatePointsByRate(int purchaseAmount, int accumulationRate) {
        // 적립률을 퍼센트로 계산하여 포인트 계산
        return (int) (purchaseAmount * (accumulationRate / 100.0));
    }

    // 포인트 갱신 메서드
    private void updateMemberPoints(Long memberId, int updatedAmount) {
        Optional<Point> optionalPoint = pointRepository.findByMember_Id(memberId);
        Point point = optionalPoint.orElseThrow(() -> new PointPolicyException("회원 포인트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        point.setAmount(updatedAmount);  // 포인트 갱신
        pointRepository.save(point);     // 변경 사항 저장
    }

    // 포인트 추가 기능
    @Override
    public ApiResponse<String> addPoints(Long memberId, int amount, PointLogUpdatedType updatedTypeEnum) {
        // 1. 회원 포인트 조회
        Optional<Point> optionalPoint = pointRepository.findByMember_Id(memberId);
        Point point = optionalPoint.orElseThrow(() -> new PointPolicyException("회원 포인트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // 2. 포인트 갱신
        int updatedAmount = point.getAmount() + amount;  // 기존 포인트에 새로운 포인트 추가
        point.setAmount(updatedAmount);
        pointRepository.save(point);

        // 3. 포인트 로그 기록
        PointLog pointLog = PointLog.builder()
                .pointLogUpdatedAt(LocalDateTime.now())
                .pointLogUpdatedType(updatedTypeEnum)  // 주어진 업데이트 타입
                .pointLogAmount(amount)
                .point(point)
                .afterPointAmount(point.getPointCurrent())
                .build();
        pointLogRepository.save(pointLog);
        return null;
    }
}
