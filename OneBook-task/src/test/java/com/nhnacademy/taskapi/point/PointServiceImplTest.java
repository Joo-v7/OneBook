package com.nhnacademy.taskapi.point;

import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.point.domain.Point;
import com.nhnacademy.taskapi.point.domain.PointLog;
import com.nhnacademy.taskapi.point.exception.PointPolicyException;
import com.nhnacademy.taskapi.point.jpa.JpaPointRepository;
import com.nhnacademy.taskapi.point.repository.PointLogRepository;
import com.nhnacademy.taskapi.point.service.impl.PointServiceImpl;
import com.nhnacademy.taskapi.roles.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PointServiceImplTest {
    @Mock
    private JpaPointRepository pointRepository;
    @Mock
    private PointLogRepository pointLogRepository;
    @InjectMocks
    private PointServiceImpl pointService;

    private Point point;
    private Member member;
    private Grade grade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Grade 객체 생성
        grade = Grade.create("Gold", 10, "Gold Level");

        // Member 객체 생성 (createNewMember 사용)
        member = Member.createNewMember(
                grade,
                "testUser",
                "testUserLogin",
                "password123",
                LocalDate.of(1990, 1, 1),
                Member.Gender.M,
                "testuser@example.com",
                "123-456-7890",
                new Role() // You should mock or create a valid Role object here if needed
        );

        // Point 객체 생성
        point = Point.builder()
                .pointCurrent(1000)  // 초기 포인트 1000
                .member(member)      // Member 객체 설정
                .build();
    }

    // 포인트 결제 테스트 (결제 금액 초과 시 예외 발생)
    @Test
    void testUsePointsForPayment_Failure_NotEnoughPoints() {
        int paymentAmount = 1500; // 현재 포인트 1000보다 많은 금액

        // findByMember_MemberId가 포인트 정보를 반환
        when(pointRepository.findByMember_Id(anyLong())).thenReturn(Optional.of(point));

        // 예외 발생 검증
        PointPolicyException exception = assertThrows(PointPolicyException.class, () -> {
            pointService.usePointsForPayment(1l, paymentAmount); // memberId는 String 타입, 이걸 Long으로 변환해서 사용
        });

        // 예외 메시지와 상태 코드 검증
        assertEquals("포인트가 부족합니다.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        // 포인트 저장이 호출되지 않음을 검증
        verify(pointRepository, times(1)).findByMember_Id(1L); // memberId 1L로 찾기
        verify(pointRepository, times(0)).save(any(Point.class));
    }

    // 포인트 결제 테스트 (결제 가능)
    @Test
    void testUsePointsForPayment_Success() {
        int paymentAmount = 500;  // 결제 금액이 500일 때
        when(pointRepository.findByMember_Id(anyLong())).thenReturn(Optional.of(point));
        when(pointRepository.save(any(Point.class))).thenReturn(point);

        // 포인트 결제
        pointService.usePointsForPayment(1l, paymentAmount); // memberId는 String 타입, 이걸 Long으로 변환해서 사용

        // 포인트가 차감되었는지 검증
        assertEquals(500, point.getPointCurrent()); // 포인트가 500으로 차감되었어야 함
        verify(pointRepository, times(2)).findByMember_Id(1L); // memberId 1L로 찾기
        verify(pointRepository, times(1)).save(any(Point.class)); // 포인트 저장이 호출되었는지
        verify(pointLogRepository, times(1)).save(any(PointLog.class)); // 포인트 로그도 저장되었는지 확인
    }
}