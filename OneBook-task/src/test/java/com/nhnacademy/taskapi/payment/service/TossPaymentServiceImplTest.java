package com.nhnacademy.taskapi.payment.service;

import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.order.entity.Order;
import com.nhnacademy.taskapi.order.entity.OrderStatus;
import com.nhnacademy.taskapi.order.repository.OrderStatusRepository;
import com.nhnacademy.taskapi.order.service.OrderService;
import com.nhnacademy.taskapi.payment.domain.Payment;
import com.nhnacademy.taskapi.payment.domain.PaymentMethod;
import com.nhnacademy.taskapi.payment.dto.toss.TossConfirmRequest;
import com.nhnacademy.taskapi.payment.dto.toss.TossConfirmResponse;
import com.nhnacademy.taskapi.payment.exception.PaymentNotFoundException;
import com.nhnacademy.taskapi.payment.repository.PaymentRepository;
import com.nhnacademy.taskapi.payment.service.impl.TossPaymentServiceImpl;
import com.nhnacademy.taskapi.roles.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TossPaymentServiceImplTest {

    @Mock
    PaymentRepository paymentRepository;
    @Mock
    OrderService orderService;
    @Mock
    CommonPaymentService commonPaymentService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    OrderStatusRepository orderStatusRepository;

    @InjectMocks
    @Spy
    TossPaymentServiceImpl tossPaymentService;

    private Member member;
    private Order testOrder;
    private OrderStatus paymentWaitStatus; // "결제대기"
    private OrderStatus shippingBeforeStatus; // "배송전"
    private Grade grade;
    private Role memberRole;
    private Role adminRole;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
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
    @DisplayName("Payment가 없으면 PaymentNotFoundException")
    void testConfirmTossPayment_whenPaymentNotFound_thenThrowException() {
        // GIVEN
        TossConfirmRequest req = new TossConfirmRequest();
        req.setOrderId("123");
        req.setPaymentKey("PK_abc");
        req.setAmount("1000");

        // paymentRepository 에서 null 반환 => PaymentNotFoundException 유발
        given(paymentRepository.findByOrder_OrderId(123L)).willReturn(null);

        // WHEN & THEN
        assertThatThrownBy(() -> tossPaymentService.confirmTossPayment(req))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("해당 주문의 결제 정보가 없습니다.");

        // verify
        then(paymentRepository).should().findByOrder_OrderId(123L);
        then(commonPaymentService).shouldHaveNoInteractions();
        then(orderService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Payment가 READY가 아니면 IllegalStateException")
    void testConfirmTossPayment_whenPaymentNotReady_thenThrow() {
        // GIVEN
        TossConfirmRequest req = new TossConfirmRequest();
        req.setOrderId("200");
        req.setPaymentKey("PK_abc");
        req.setAmount("5000");

        Payment notReadyPayment = new Payment();
        notReadyPayment.setStatus("DONE"); // READY가 아님

        given(paymentRepository.findByOrder_OrderId(200L)).willReturn(notReadyPayment);

        // WHEN & THEN
        assertThatThrownBy(() -> tossPaymentService.confirmTossPayment(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 결제완료(DONE) 혹은 취소 상태입니다.");

        then(commonPaymentService).shouldHaveNoInteractions();
        then(orderService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("승인 요청 금액과 DB의 Payment 금액 불일치 -> IllegalStateException")
    void testConfirmTossPayment_whenApproveAmountMismatch_thenThrow() {
        // GIVEN
        TossConfirmRequest req = new TossConfirmRequest();
        req.setOrderId("300");
        req.setPaymentKey("PK_xyz");
        req.setAmount("10000"); // 클라이언트서 넘어온 "승인요청금액" 문자열

        Payment payment = new Payment();
        payment.setStatus("READY");
        payment.setTotalAmount(5000); // DB에는 5천원으로 저장

        given(paymentRepository.findByOrder_OrderId(300L)).willReturn(payment);

        // WHEN & THEN
        assertThatThrownBy(() -> tossPaymentService.confirmTossPayment(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("승인 요청 금액과 DB 금액이 다릅니다.");

        then(commonPaymentService).shouldHaveNoInteractions();
        then(orderService).shouldHaveNoInteractions();
    }


    @Test
    @DisplayName("orderId에 _ 포함 & 숫자 변환 불가능 => PaymentNotFoundException")
    void testConfirmTossPayment_whenInvalidOrderId() {
        // GIVEN
        TossConfirmRequest req = new TossConfirmRequest();
        req.setOrderId("ABC_zzz");
        req.setPaymentKey("PK_abc");
        req.setAmount("1000");

        // WHEN & THEN
        assertThatThrownBy(() -> tossPaymentService.confirmTossPayment(req))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("유효하지 않은 orderId 형식입니다.");
    }

    @Test
    @DisplayName("성공적인 토스 결제 승인")
    void testConfirmTossPayment_successful() throws Exception {
        // GIVEN
        TossConfirmRequest req = new TossConfirmRequest();
        req.setOrderId("400");
        req.setPaymentKey("PK_success");
        req.setAmount("5000");

        Payment payment = new Payment();
        payment.setStatus("READY");
        payment.setTotalAmount(5000);
        payment.setOrder(testOrder);

        given(paymentRepository.findByOrder_OrderId(400L)).willReturn(payment);

        // 모킹할 Toss API 응답 JSON
        String jsonResponse = "{"
                + "\"status\":\"DONE\","
                + "\"approvedAt\":\"2023-01-01T10:00:00\","
                + "\"method\":\"CARD\","
                + "\"card\":{"
                + "    \"ownerType\":\"PERSONAL\","
                + "    \"number\":\"1234-****-****-5678\","
                + "    \"amount\":5000,"
                + "    \"issuerCode\":\"ABC\","
                + "    \"acquirerCode\":\"XYZ\","
                + "    \"isInterestFree\":true,"
                + "    \"cardType\":\"VISA\","
                + "    \"approveNo\":\"APPROVE123\","
                + "    \"installmentPlanMonths\":0"
                + "}"
                + "}";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        given(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .willReturn(responseEntity);

        // WHEN
        TossConfirmResponse response = tossPaymentService.confirmTossPayment(req);

        // THEN
        assertThat(response.getPaymentKey()).isEqualTo("PK_success");
        assertThat(response.getOrderId()).isEqualTo("400");
        assertThat(response.getStatus()).isEqualTo("DONE");
        assertThat(response.getApprovedAt()).isEqualTo(LocalDateTime.parse("2023-01-01T10:00:00"));
        assertThat(response.getMessage()).isEqualTo("결제 승인 성공");

        // Payment, PaymentMethod 상태 업데이트 검증
        then(commonPaymentService).should().usedPurchasePoint(payment);
        then(commonPaymentService).should().accumulationPurchasePoints(payment);
        then(orderService).should().updateOrderStatus(List.of(400L), "배송전");
        then(paymentRepository).should().save(payment);
    }

    @Test
    @DisplayName("유효하지 않은 결제 금액 형식 -> IllegalArgumentException")
    void testConfirmTossPayment_invalidAmountFormat_thenThrow() {
        TossConfirmRequest req = new TossConfirmRequest();
        req.setOrderId("500");
        req.setPaymentKey("PK_invalid");
        req.setAmount("invalid_amount");

        given(paymentRepository.findByOrder_OrderId(500L)).willReturn(new Payment());

        assertThatThrownBy(() -> tossPaymentService.confirmTossPayment(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 결제 금액입니다.");
    }

    @Test
    @DisplayName("유효한 orderId 형식 파싱")
    void testParseOrderId_validFormat() {
        // 예시: "123_ABC" -> 123 반환
        Long result = tossPaymentService.parseOrderId("123_ABC");
        assertThat(result).isEqualTo(123L);
    }

    @Test
    @DisplayName("유효하지 않은 orderId 형식 파싱 -> PaymentNotFoundException")
    void testParseOrderId_invalidFormat() {
        assertThatThrownBy(() -> tossPaymentService.parseOrderId("집에가고싶습니다_IWantGoHome"))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("유효하지 않은 orderId 형식입니다.");
    }

    @Test
    @DisplayName("토스 API 응답 상태 코드 2xx가 아닌 경우 RuntimeException 발생")
    void testConfirmTossPayment_apiNon2xxResponse_thenThrow() {
        // GIVEN
        TossConfirmRequest req = new TossConfirmRequest();
        req.setOrderId("600");
        req.setPaymentKey("PK_non2xx");
        req.setAmount("3000");

        Payment payment = new Payment();
        payment.setStatus("READY");
        payment.setTotalAmount(3000);
        payment.setOrder(testOrder);

        given(paymentRepository.findByOrder_OrderId(600L)).willReturn(payment);

        // Toss API 응답 상태 코드가 실패일 때 모킹
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Error Response", HttpStatus.BAD_REQUEST);
        given(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .willReturn(responseEntity);

        // WHEN & THEN
        assertThatThrownBy(() -> tossPaymentService.confirmTossPayment(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("토스 결제승인 실패");
    }

    @Test
    @DisplayName("응답 JSON 파싱 오류 발생 시 RuntimeException 발생")
    void testConfirmTossPayment_jsonParsingError_thenThrow() {
        // GIVEN
        TossConfirmRequest req = new TossConfirmRequest();
        req.setOrderId("700");
        req.setPaymentKey("PK_parseError");
        req.setAmount("4000");

        Payment payment = new Payment();
        payment.setStatus("READY");
        payment.setTotalAmount(4000);
        payment.setOrder(testOrder);

        given(paymentRepository.findByOrder_OrderId(700L)).willReturn(payment);

        // 올바르지 않은 JSON 형식 반환
        String invalidJsonResponse = "Invalid JSON";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(invalidJsonResponse, HttpStatus.OK);
        given(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .willReturn(responseEntity);

        // WHEN & THEN
        assertThatThrownBy(() -> tossPaymentService.confirmTossPayment(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("응답 JSON 파싱 오류");
    }

    @Test
    @DisplayName("PaymentMethod가 존재하고 다른 paymentKey를 가진 경우 IllegalStateException 발생")
    void testConfirmTossPayment_existingPaymentMethodDifferentKey_thenThrow() {
        // GIVEN
        TossConfirmRequest req = new TossConfirmRequest();
        req.setOrderId("800");
        req.setPaymentKey("PK_new");
        req.setAmount("6000");

        Payment payment = new Payment();
        payment.setStatus("READY");
        payment.setTotalAmount(6000);
        payment.setOrder(testOrder);

        PaymentMethod existingPm = new PaymentMethod();
        existingPm.setPaymentKey("PK_old");
        payment.setPaymentMethod(existingPm);

        given(paymentRepository.findByOrder_OrderId(800L)).willReturn(payment);

        String jsonResponse = "{"
                + "\"status\":\"DONE\","
                + "\"approvedAt\":\"2023-01-01T10:00:00\","
                + "\"method\":\"CARD\","
                + "\"card\":{"
                + "    \"ownerType\":\"PERSONAL\","
                + "    \"number\":\"1234-****-****-5678\","
                + "    \"amount\":6000,"
                + "    \"issuerCode\":\"ABC\","
                + "    \"acquirerCode\":\"XYZ\","
                + "    \"isInterestFree\":true,"
                + "    \"cardType\":\"VISA\","
                + "    \"approveNo\":\"APPROVE123\","
                + "    \"installmentPlanMonths\":0"
                + "}"
                + "}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        given(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .willReturn(responseEntity);

        // WHEN & THEN
        assertThatThrownBy(() -> tossPaymentService.confirmTossPayment(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("기존 PaymentMethod와 다른 paymentKey가 들어왔습니다.");
    }


    @Test
    @DisplayName("Toss 응답 cardObj가 null일 경우 PaymentMethod 업데이트")
    void testConfirmTossPayment_whenCardObjNull_thenSetCashType() throws Exception {
        // GIVEN
        TossConfirmRequest req = new TossConfirmRequest();
        req.setOrderId("900");
        req.setPaymentKey("PK_cash");
        req.setAmount("7000");

        Payment payment = new Payment();
        payment.setStatus("READY");
        payment.setTotalAmount(7000);
        payment.setOrder(testOrder);

        given(paymentRepository.findByOrder_OrderId(900L)).willReturn(payment);

        // cardObj를 null로 설정한 JSON 응답 구성
        String jsonResponse = "{"
                + "\"status\":\"DONE\","
                + "\"approvedAt\":\"2023-02-01T10:00:00\","
                + "\"method\":\"CASH\""
                + "}";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        given(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .willReturn(responseEntity);

        // WHEN
        TossConfirmResponse response = tossPaymentService.confirmTossPayment(req);

        // THEN
        // PaymentMethod가 CASH 타입으로 설정되었는지 확인
        PaymentMethod pm = payment.getPaymentMethod();
        assertThat(pm).isNotNull();
        assertThat(pm.getCardType()).isEqualTo("CASH");
        assertThat(pm.getCardOwnerType()).isEqualTo("PERSONAL");

        // SUCCESS 응답 검증
        assertThat(response.getStatus()).isEqualTo("DONE");
    }


    @Test
    @DisplayName("approvedAt 날짜 파싱 실패 시 현재 시간 사용")
    void testConfirmTossPayment_dateTimeParseException_thenUseNow() throws Exception {
        // GIVEN
        TossConfirmRequest req = new TossConfirmRequest();
        req.setOrderId("1000");
        req.setPaymentKey("PK_dateError");
        req.setAmount("8000");

        Payment payment = new Payment();
        payment.setStatus("READY");
        payment.setTotalAmount(8000);
        payment.setOrder(testOrder);

        given(paymentRepository.findByOrder_OrderId(1000L)).willReturn(payment);

        // 잘못된 approvedAt 값을 가지는 JSON 응답
        String jsonResponse = "{"
                + "\"status\":\"DONE\","
                + "\"approvedAt\":\"invalid-date-format\","
                + "\"method\":\"CARD\","
                + "\"card\":null"
                + "}";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        given(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .willReturn(responseEntity);

        // WHEN
        TossConfirmResponse response = tossPaymentService.confirmTossPayment(req);

        // THEN
        // approvedAt가 현재 시간 근처로 설정되었는지 확인 (테스트는 오차 허용)
        LocalDateTime now = LocalDateTime.now();
        assertThat(response.getApprovedAt()).isCloseTo(now,within(5, ChronoUnit.SECONDS));
    }

}
