package com.nhnacademy.taskapi.payment.service;

import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.order.entity.Order;
import com.nhnacademy.taskapi.order.entity.OrderStatus;
import com.nhnacademy.taskapi.order.repository.OrderRepository;
import com.nhnacademy.taskapi.order.repository.OrderStatusRepository;
import com.nhnacademy.taskapi.order.service.OrderService;
import com.nhnacademy.taskapi.payment.domain.Payment;
import com.nhnacademy.taskapi.payment.domain.PaymentMethod;
import com.nhnacademy.taskapi.payment.dto.CheckoutInfoResponse;
import com.nhnacademy.taskapi.payment.dto.PaymentRequest;
import com.nhnacademy.taskapi.payment.dto.PaymentResponse;
import com.nhnacademy.taskapi.payment.exception.InvalidPaymentException;
import com.nhnacademy.taskapi.payment.exception.PaymentNotFoundException;
import com.nhnacademy.taskapi.payment.repository.PaymentRepository;
import com.nhnacademy.taskapi.payment.service.CommonPaymentService;
import com.nhnacademy.taskapi.payment.service.impl.PaymentServiceImpl;
import com.nhnacademy.taskapi.point.domain.Point;
import com.nhnacademy.taskapi.point.jpa.JpaPointRepository;
import com.nhnacademy.taskapi.point.service.PointService;
import com.nhnacademy.taskapi.roles.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderStatusRepository orderStatusRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JpaPointRepository pointRepository;
    @Mock
    private CommonPaymentService commonPaymentService;
    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Member member;
    private Order testOrder;
    private OrderStatus paymentWaitStatus; // "결제대기"
    private OrderStatus shippingBeforeStatus; // "배송전"
    private Grade grade;
    private Role memberRole;
    private Role adminRole;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        // Grade 객체 생성
        grade = Grade.create("Gold", 10, "WoW, You Are BlackCow");

        // MEMBER Role 생성
        memberRole = Role.createRole("MEMBER", "내 돈줄");
        Field memberRoleIdField = Role.class.getDeclaredField("id");
        memberRoleIdField.setAccessible(true);
        memberRoleIdField.set(memberRole, 1); // ID를 Long 타입으로 설정

        // ADMIN Role 생성
        adminRole = Role.createRole("ADMIN", "Administrator role");
        Field adminRoleIdField = Role.class.getDeclaredField("id");
        adminRoleIdField.setAccessible(true);
        adminRoleIdField.set(adminRole, 2); // ID를 Long 타입으로 설정

        // Member(일반 사용자) 객체 생성
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

        // Order
        testOrder = new Order();
        testOrder.setOrderId(100L);
        testOrder.setMember(member);
        testOrder.setTotalPrice(10000);
        testOrder.setDeliveryPrice(3000);
        testOrder.setPackagingPrice(1000);
        testOrder.setOrderName("책주문");
        testOrder.setOrdererName("집가고싶다");
        testOrder.setOrdererPhoneNumber("010-1111-2222");

        // OrderStatus
        paymentWaitStatus = new OrderStatus("결제대기");
        shippingBeforeStatus = new OrderStatus("배송전");

        testOrder.setOrderStatus(paymentWaitStatus);

        // 기본 Mock 설정
        lenient().when(orderStatusRepository.findByStatusName("결제대기"))
                .thenReturn(Optional.of(paymentWaitStatus));
        lenient().when(orderStatusRepository.findByStatusName("배송전"))
                .thenReturn(Optional.of(shippingBeforeStatus));

        Field orderStatusIdField = OrderStatus.class.getDeclaredField("orderStatusId");
        orderStatusIdField.setAccessible(true);
        orderStatusIdField.setInt(paymentWaitStatus, 1);
        orderStatusIdField.setInt(shippingBeforeStatus, 2);
    }

    @Test
    @DisplayName("createPayment - 기존에 READY 결제 존재하고, 새 포인트 변경(정상)")
    void testCreatePayment_whenAlreadyReadyPaymentExists_andUpdatePoints() {
        // GIVEN
        Payment existingPayment = new Payment();
        existingPayment.setPaymentId(999L);
        existingPayment.setStatus("READY");
        existingPayment.setOrder(testOrder);
        existingPayment.setTotalAmount(99999);
        existingPayment.setPoint(0);
        existingPayment.setRequestedAt(LocalDateTime.now());
        existingPayment.setOnlyBookAmount(10000);

        PaymentRequest request = new PaymentRequest();
        request.setUsedPoint(5000);   // 새로 사용할 포인트
        request.setCurrency("KRW");

        // 이미 존재하는 Payment
        given(paymentRepository.findByOrder_OrderId(testOrder.getOrderId()))
                .willReturn(existingPayment);

        // WHEN
        PaymentResponse response = paymentService.createPayment(
                String.valueOf(testOrder.getOrderId()),
                member.getId(),
                request
        );

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getPaymentId()).isEqualTo(999L);
        assertThat(response.getStatus()).isEqualTo("READY");
        assertThat(response.getUsePoint()).isEqualTo(5000);
        assertThat(response.getTotalAmount()).isEqualTo(10000 + 3000 + 1000 - 5000); // 9000
        assertThat(response.getOnlyBookAmount()).isEqualTo(10000 - 5000); // 5000

        // 결제정보가 save 되었는지 검증
        verify(paymentRepository, times(1)).save(any(Payment.class));
        // 외부 결제(토스 등) 로직으로 넘어가진 않고(상태가 READY), handleFullPointPayment 호출도 없음을 확인
        verify(commonPaymentService, never()).handleFullPointPayment(any(Payment.class), anyLong());
    }

    @Test
    @DisplayName("createPayment - 기존에 READY 결제 존재하지만, 포인트가 주문금액 초과")
    void testCreatePayment_whenAlreadyReadyPaymentExists_andPointExceed() {
        // GIVEN
        Payment existingPayment = new Payment();
        existingPayment.setPaymentId(999L);
        existingPayment.setStatus("READY");
        existingPayment.setOrder(testOrder);
        existingPayment.setTotalAmount(99999);
        existingPayment.setPoint(0);
        existingPayment.setRequestedAt(LocalDateTime.now());
        existingPayment.setOnlyBookAmount(10000);

        PaymentRequest request = new PaymentRequest();
        request.setUsedPoint(9999999);
        request.setCurrency("KRW");

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(testOrder));

        // WHEN & THEN
        assertThatThrownBy(() -> paymentService.createPayment(
                String.valueOf(testOrder.getOrderId()),
                member.getId(),
                request))
                .isInstanceOf(InvalidPaymentException.class)
                .hasMessageContaining("포인트 사용액이 주문 금액을 초과합니다.");

        verify(paymentRepository, never()).save(any(Payment.class));
    }



    @Test
    @DisplayName("createPayment - 이미 DONE인 Payment가 있으면 예외")
    void testCreatePayment_whenAlreadyDonePaymentExists_thenThrowException() {
        // GIVEN
        Payment existingPayment = new Payment();
        existingPayment.setPaymentId(999L);
        existingPayment.setStatus("DONE");  // 이미 결제 완료
        existingPayment.setOrder(testOrder);
        existingPayment.setTotalAmount(13000);
        existingPayment.setRequestedAt(LocalDateTime.now());

        PaymentRequest request = new PaymentRequest();
        request.setUsedPoint(1000);
        request.setCurrency("KRW");

        given(paymentRepository.findByOrder_OrderId(testOrder.getOrderId()))
                .willReturn(existingPayment);

        // WHEN & THEN
        assertThatThrownBy(() -> paymentService.createPayment(
                String.valueOf(testOrder.getOrderId()),
                member.getId(),
                request))
                .isInstanceOf(InvalidPaymentException.class)
                .hasMessageContaining("이미 결제된 (DONE 등) 주문입니다.");

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("createPayment - 정상적으로 처음 Payment 생성(포인트 일부, 결제금액 > 0인 경우)")
    void testCreatePayment_whenNoPaymentExists_andPaymentProceed() {
        // GIVEN
        PaymentRequest request = new PaymentRequest();
        request.setUsedPoint(3000);
        request.setCurrency("KRW");

        // order 상태: 결제대기
        paymentWaitStatus = spy(paymentWaitStatus);
        testOrder.setOrderStatus(paymentWaitStatus);

        given(orderRepository.findById(testOrder.getOrderId()))
                .willReturn(Optional.of(testOrder));
        given(paymentRepository.findByOrder_OrderId(testOrder.getOrderId()))
                .willReturn(null); // 이미 존재하는 Payment 없음
        given(paymentRepository.save(any(Payment.class))).willAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setPaymentId(999L);
            return p;
        });

        // WHEN
        PaymentResponse response = paymentService.createPayment(
                String.valueOf(testOrder.getOrderId()),
                member.getId(),
                request
        );

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getPaymentId()).isEqualTo(999L);
        assertThat(response.getStatus()).isEqualTo("READY"); // 포인트 일부만 사용 => 나머지는 실제 결제 필요
        assertThat(response.getUsePoint()).isEqualTo(3000);
        assertThat(response.getTotalAmount()).isEqualTo(10000 + 3000 + 1000 - 3000); // 11000
        assertThat(response.getOnlyBookAmount()).isEqualTo(10000 - 3000); // 7000

        // READY 이므로 외부 결제 로직 등 진행
        verify(commonPaymentService, never()).handleFullPointPayment(any(), anyLong());
        verify(orderService, never()).updateOrderStatus(anyList(), anyString());
    }

    @Test
    @DisplayName("createPayment - 전액 포인트 결제인 경우 즉시 DONE 처리")
    void testCreatePayment_whenNoPaymentExists_andFullPointPayment() {
        // GIVEN
        PaymentRequest request = new PaymentRequest();
        request.setUsedPoint(14000); // totalPrice(10000) + delivery(3000) + packaging(1000) = 14000

        paymentWaitStatus = spy(paymentWaitStatus);
        testOrder.setOrderStatus(paymentWaitStatus);

        given(orderRepository.findById(testOrder.getOrderId()))
                .willReturn(Optional.of(testOrder));
        given(paymentRepository.findByOrder_OrderId(testOrder.getOrderId()))
                .willReturn(null);
        given(paymentRepository.save(any(Payment.class))).willAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setPaymentId(123L);
            return p;
        });

        // WHEN
        PaymentResponse response = paymentService.createPayment(
                String.valueOf(testOrder.getOrderId()),
                member.getId(),
                request
        );

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getPaymentId()).isEqualTo(123L);
        // 전액 포인트 결제 => status == DONE
        assertThat(response.getStatus()).isEqualTo("DONE");
        // orderService.updateOrderStatus() 호출
        verify(orderService, times(1))
                .updateOrderStatus(List.of(testOrder.getOrderId()), "배송전");
        // 포인트 차감 로직(전액 포인트) 호출
        verify(commonPaymentService, times(1))
                .handleFullPointPayment(any(Payment.class), eq(member.getId()));
    }

    @Test
    @DisplayName("getPayment - 정상 조회")
    void testGetPayment_whenPaymentExists() {
        // GIVEN
        Payment payment = new Payment();
        payment.setPaymentId(987L);
        payment.setOrder(testOrder);
        payment.setPaymentMethod(null);
        payment.setStatus("DONE");
        payment.setRequestedAt(LocalDateTime.now());
        payment.setApprovedAt(LocalDateTime.now());
        payment.setPoint(5000);
        payment.setTotalAmount(10000);

        given(paymentRepository.findById(987L)).willReturn(Optional.of(payment));

        // WHEN
        PaymentResponse response = paymentService.getPayment(987L);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getPaymentId()).isEqualTo(987L);
        assertThat(response.getStatus()).isEqualTo("DONE");
        assertThat(response.getUsePoint()).isEqualTo(5000);
        assertThat(response.getTotalAmount()).isEqualTo(10000);
    }

    @Test
    @DisplayName("getPayment - Payment 없으면 예외")
    void testGetPayment_whenPaymentNotFound() {
        // GIVEN
        given(paymentRepository.findById(anyLong())).willReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> paymentService.getPayment(123L))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("결제 정보를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("getCheckoutInfo - 정상 조회")
    void testGetCheckoutInfo_whenValid() {
        // GIVEN
        Point memberPoint = Point.builder()
                .member(member)
                .pointCurrent(50000)
                .build();

        testOrder.setOrderName("책주문");
        testOrder.setDeliveryPrice(3000);
        testOrder.setPackagingPrice(1000);
        testOrder.setTotalPrice(10000);

        given(orderRepository.findById(testOrder.getOrderId()))
                .willReturn(Optional.of(testOrder));
        given(pointRepository.findByMember_Id(member.getId()))
                .willReturn(Optional.of(memberPoint));
        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));

        // WHEN
        CheckoutInfoResponse response = paymentService.getCheckoutInfo(String.valueOf(testOrder.getOrderId()), member.getId());

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getOrderId()).isEqualTo("100");
        assertThat(response.getOrderAmount()).isEqualTo(10000 + 3000 + 1000);
        assertThat(response.getOrdererName()).isEqualTo("집가고싶다");
        assertThat(response.getOrderName()).isEqualTo("책주문");
        assertThat(response.getUserPoint()).isEqualTo(50000);
    }

    @Test
    @DisplayName("getCheckoutInfo - 주문이 다른 회원 것인 경우 예외")
    void testGetCheckoutInfo_whenOrderNotBelongToMember() {
        // GIVEN
        Member anotherMember = new Member();
        anotherMember = spy(anotherMember);
        doReturn(2L).when(anotherMember).getId();

        testOrder.setMember(anotherMember);

        given(orderRepository.findById(testOrder.getOrderId()))
                .willReturn(Optional.of(testOrder));

        // WHEN & THEN
        assertThatThrownBy(() -> paymentService.getCheckoutInfo(String.valueOf(testOrder.getOrderId()), member.getId()))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("본인 주문이 아닙니다.");
    }

    @Test
    @DisplayName("createPayment - orderId 형식이 잘못된 경우 예외")
    void testCreatePayment_invalidOrderIdFormat() {
        // 잘못된 문자열
        String invalidOrderIdStr = "집에가고싶습니다_IWantGoHome";

        PaymentRequest request = new PaymentRequest();
        request.setUsedPoint(500);

        // WHEN & THEN
        assertThatThrownBy(() -> paymentService.createPayment(invalidOrderIdStr, member.getId(), request))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("유효하지 않은 orderId 형식입니다.");
    }
}
