package com.nhnacademy.taskapi.payment.service;

import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.order.entity.Order;
import com.nhnacademy.taskapi.order.entity.OrderStatus;
import com.nhnacademy.taskapi.payment.domain.Payment;
import com.nhnacademy.taskapi.payment.domain.PaymentMethod;
import com.nhnacademy.taskapi.payment.exception.InsufficientPointException;
import com.nhnacademy.taskapi.payment.exception.InvalidPaymentException;
import com.nhnacademy.taskapi.payment.exception.PaymentNotFoundException;
import com.nhnacademy.taskapi.point.domain.Point;
import com.nhnacademy.taskapi.point.jpa.JpaPointRepository;
import com.nhnacademy.taskapi.point.service.PointService;
import com.nhnacademy.taskapi.roles.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class CommonPaymentServiceTest {

    @Mock
    JpaPointRepository pointRepository;
    @Mock
    PointService pointService;
    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    private CommonPaymentService commonPaymentService;

    private Payment testPayment;
    private Point testUserPoint
            ;

    private Member member;
    private Order testOrder;
    private Grade grade;
    private Role memberRole;
    private Role adminRole;


    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        grade = Grade.create("Gold", 10, "WoW, You Are BlackCow");

        memberRole = Role.createRole("MEMBER", "내 돈줄");
        Field memberRoleIdField = Role.class.getDeclaredField("id");
        memberRoleIdField.setAccessible(true);
        memberRoleIdField.set(memberRole, 1);

        adminRole = Role.createRole("ADMIN", "Administrator role");
        Field adminRoleIdField = Role.class.getDeclaredField("id");
        adminRoleIdField.setAccessible(true);
        adminRoleIdField.set(adminRole, 2);

        member = Member.createNewMember(
                grade,
                "집가고싶다",
                "wantgohome",
                "showmethemoney",
                LocalDate.of(1990, 1, 1),
                Member.Gender.M,
                "ebul@outside.dangerous",
                "123-456-7890",
                memberRole
        );
        Field memberIdField = Member.class.getDeclaredField("id");
        memberIdField.setAccessible(true);
        memberIdField.set(member, 1L);
        member.setStatus(Member.Status.ACTIVE);

        testOrder = new Order();
        testOrder.setMember(member);
        testOrder.setTotalPrice(10000);

        testPayment = new Payment();
        testPayment.setOrder(testOrder);
        testPayment.setPoint(3000);   // 포인트 3000 사용
        testPayment.setStatus("READY");

        testUserPoint
                = Point.builder()
                .member(member)
                .pointCurrent(10000)
                .build();

    }

    @Test
    @DisplayName("handleFullPointPayment - 정상(충분한 포인트) -> 전액 포인트 결제 완료")
    void testHandleFullPointPayment_success() {
        // GIVEN
        given(pointRepository.findByMember_Id(member.getId()))
                .willReturn(Optional.of(testUserPoint
                ));

        // WHEN
        commonPaymentService.handleFullPointPayment(testPayment, member.getId());

        // THEN
        // 1) 포인트 차감
        then(pointService).should(times(1))
                .usePointsForPayment(member.getId(), 3000);
        // 2) PaymentMethod가 "POINT" + paymentKey 생성
        PaymentMethod pm = testPayment.getPaymentMethod();
        assertThat(pm).isNotNull();
        assertThat(pm.getType()).isEqualTo("POINT");
        assertThat(pm.getMethod()).isEqualTo("POINT");
        assertThat(pm.getPaymentKey()).contains("POINT_");  // 예: "POINT_2025..."
        // 3) Payment 상태
        assertThat(testPayment.getStatus()).isEqualTo("DONE");
        assertThat(testPayment.getApprovedAt()).isNotNull();
    }

    @Test
    @DisplayName("handleFullPointPayment - 포인트 정보 없음 -> InvalidPaymentException")
    void testHandleFullPointPayment_noPointInfo() {
        // GIVEN
        given(pointRepository.findByMember_Id(member.getId()))
                .willReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(
                () -> commonPaymentService.handleFullPointPayment(testPayment, member.getId())
        ).isInstanceOf(InvalidPaymentException.class)
                .hasMessageContaining("포인트 정보가 없습니다.");

        then(pointService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("handleFullPointPayment - 포인트 부족 -> InsufficientPointException")
    void testHandleFullPointPayment_insufficientPoints() {
        // GIVEN
        testPayment.setPoint(15000); // 사용 포인트 15000
        testUserPoint
                .setAmount(10000);
        given(pointRepository.findByMember_Id(member.getId()))
                .willReturn(Optional.of(testUserPoint
                ));

        // WHEN & THEN
        assertThatThrownBy(() ->
                commonPaymentService.handleFullPointPayment(testPayment, member.getId()))
                .isInstanceOf(InsufficientPointException.class)
                .hasMessageContaining("보유 포인트가 부족");

        // 포인트 차감 메서드는 호출되지 않음
        then(pointService).should(never()).usePointsForPayment(anyLong(), anyInt());
    }


    @Test
    @DisplayName("accumulationPurchasePoints - 순수 구매금액 > 0 인 경우, 포인트 적립")
    void testAccumulationPurchasePoints_success() {
        // GIVEN
        testOrder.setTotalPrice(10000);
        testPayment.setPoint(3000);
        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));

        // WHEN
        commonPaymentService.accumulationPurchasePoints(testPayment);

        // THEN
        then(pointService).should(times(1))
                .registerPurchasePoints(member, 7000);
    }

    @Test
    @DisplayName("accumulationPurchasePoints - 순수 책금액 <=0 이면 포인트 적립 없음")
    void testAccumulationPurchasePoints_noAccumulateIfZeroOrLess() {
        // GIVEN
        testOrder.setTotalPrice(3000);
        testPayment.setPoint(5000);

        // WHEN
        commonPaymentService.accumulationPurchasePoints(testPayment);

        // THEN
        // onlyBookAmount<=0 => 적립 로직 호출하지 않음
        then(memberRepository).shouldHaveNoInteractions();
        then(pointService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("accumulationPurchasePoints - memberRepository에서 회원 못 찾으면 예외")
    void testAccumulationPurchasePoints_noMember() {
        // GIVEN
        // totalPrice=10000, payment point=3000 => onlyBookAmount=7000
        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> commonPaymentService.accumulationPurchasePoints(testPayment))
                .isInstanceOf(InvalidPaymentException.class)
                .hasMessageContaining("회원을 찾을 수 없습니다.");
    }


    @Test
    @DisplayName("usedPurchasePoint - 사용 포인트가 0이면 아무 일도 안 함")
    void testUsedPurchasePoint_zeroPoint() {
        // GIVEN
        testPayment.setPoint(0);

        // WHEN
        commonPaymentService.usedPurchasePoint(testPayment);

        // THEN
        // pointRepository.findByMember_Id 호출 X
        then(pointRepository).shouldHaveNoInteractions();
        then(pointService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("usedPurchasePoint - 회원 포인트 정보 없음 -> PaymentNotFoundException")
    void testUsedPurchasePoint_noPointInfo() {
        // GIVEN
        // payment.point=3000
        given(pointRepository.findByMember_Id(member.getId()))
                .willReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> commonPaymentService.usedPurchasePoint(testPayment))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("회원 포인트를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("usedPurchasePoint - 포인트 부족 -> InsufficientPointException")
    void testUsedPurchasePoint_insufficientPoints() {
        // GIVEN
        testPayment.setPoint(20000);
        testUserPoint
                .setAmount(10000);
        given(pointRepository.findByMember_Id(member.getId()))
                .willReturn(Optional.of(testUserPoint
                ));

        // WHEN & THEN
        assertThatThrownBy(() -> commonPaymentService.usedPurchasePoint(testPayment))
                .isInstanceOf(InsufficientPointException.class)
                .hasMessageContaining("포인트가 부족");

        // 포인트 차감은 호출되지 않음
        then(pointService).should(never()).usePointsForPayment(anyLong(), anyInt());
    }

    @Test
    @DisplayName("usedPurchasePoint - 정상 차감")
    void testUsedPurchasePoint_success() {
        // GIVEN
        testPayment.setPoint(3000);
        testUserPoint
                .setAmount(5000);
        given(pointRepository.findByMember_Id(member.getId()))
                .willReturn(Optional.of(testUserPoint
                ));

        // WHEN
        commonPaymentService.usedPurchasePoint(testPayment);

        // THEN
        then(pointService).should(times(1))
                .usePointsForPayment(member.getId(), 3000);
    }
}
