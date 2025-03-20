package com.nhnacademy.taskapi.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.payment.dto.CheckoutInfoResponse;
import com.nhnacademy.taskapi.payment.dto.PaymentRequest;
import com.nhnacademy.taskapi.payment.dto.PaymentResponse;
import com.nhnacademy.taskapi.payment.dto.toss.TossConfirmRequest;
import com.nhnacademy.taskapi.payment.dto.toss.TossConfirmResponse;
import com.nhnacademy.taskapi.payment.exception.PaymentNotFoundException;
import com.nhnacademy.taskapi.payment.service.PaymentService;
import com.nhnacademy.taskapi.payment.service.impl.TossPaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;
    @MockBean
    private TossPaymentServiceImpl tossPaymentService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    // [POST] /task/payments/{orderId}
    @Test
    @DisplayName("createPayment - 정상 케이스")
    void testCreatePayment_ok() throws Exception {
        // GIVEN
        String orderIdStr = "100";
        Long memberId = 99L;

        PaymentRequest req = new PaymentRequest();
        req.setUsedPoint(3000);
        req.setCurrency("KRW");

        PaymentResponse mockResp = PaymentResponse.builder()
                .paymentId(1000L)
                .orderId(orderIdStr)
                .status("READY")
                .usePoint(3000)
                .totalAmount(11000)
                .build();

        given(paymentService.createPayment(eq(orderIdStr), eq(memberId), any(PaymentRequest.class)))
                .willReturn(mockResp);

        // WHEN & THEN
        mockMvc.perform(post("/task/payments/{orderId}", orderIdStr)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-MEMBER-ID", memberId)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentId").value(1000L))
                .andExpect(jsonPath("$.orderId").value("100"))
                .andExpect(jsonPath("$.status").value("READY"))
                .andExpect(jsonPath("$.usePoint").value(3000))
                .andExpect(jsonPath("$.totalAmount").value(11000));
    }

    // [GET] /task/payments/{paymentId}
    @Test
    @DisplayName("getPayment - 정상 조회")
    void testGetPayment_ok() throws Exception {
        // GIVEN
        Long paymentId = 1234L;
        PaymentResponse mockResp = PaymentResponse.builder()
                .paymentId(paymentId)
                .status("DONE")
                .totalAmount(14000)
                .build();

        given(paymentService.getPayment(paymentId)).willReturn(mockResp);

        // WHEN & THEN
        mockMvc.perform(get("/task/payments/{paymentId}", paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(1234L))
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.totalAmount").value(14000));
    }

    @Test
    @DisplayName("getPayment - PaymentNotFoundException 발생 시 404")
    void testGetPayment_notFound() throws Exception {
        // GIVEN
        Long paymentId = 9999L;
        given(paymentService.getPayment(paymentId)).willThrow(new PaymentNotFoundException("결제 정보를 찾을 수 없습니다."));

        // WHEN & THEN
        mockMvc.perform(get("/task/payments/{paymentId}", paymentId))
                .andExpect(status().isNotFound());
    }

    // [POST] /task/payments/toss/confirm
    @Test
    @DisplayName("confirmToss - 정상 승인")
    void testConfirmToss_ok() throws Exception {
        // GIVEN
        Long memberId = 50L;
        TossConfirmRequest req = new TossConfirmRequest();
        req.setPaymentKey("PK_abc");
        req.setOrderId("123_xyz");
        req.setAmount("8000");

        TossConfirmResponse mockRes = new TossConfirmResponse();
        mockRes.setPaymentKey("PK_abc");
        mockRes.setOrderId("123_xyz");
        mockRes.setStatus("DONE");
        mockRes.setMessage("결제 승인 성공");
        mockRes.setMemberId(memberId);

        given(tossPaymentService.confirmTossPayment(any(TossConfirmRequest.class)))
                .willReturn(mockRes);

        // WHEN & THEN
        mockMvc.perform(post("/task/payments/toss/confirm")
                        .header("X-MEMBER-ID", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentKey").value("PK_abc"))
                .andExpect(jsonPath("$.orderId").value("123_xyz"))
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.message").value("결제 승인 성공"))
                .andExpect(jsonPath("$.memberId").value(50L));
    }

    // [GET] /task/payments/checkout-info/{orderId}
    @Test
    @DisplayName("getCheckoutInfo - 정상 조회")
    void testGetCheckoutInfo_ok() throws Exception {
        // GIVEN
        String orderIdStr = "100_abc";
        Long memberId = 10L;

        CheckoutInfoResponse mockRes = new CheckoutInfoResponse();
        mockRes.setOrderId(orderIdStr);
        mockRes.setOrderAmount(14000);
        mockRes.setUserPoint(50000);
        mockRes.setOrderName("책주문");
        mockRes.setOrdererName("테스터");

        given(paymentService.getCheckoutInfo(orderIdStr, memberId))
                .willReturn(mockRes);

        // WHEN & THEN
        mockMvc.perform(get("/task/payments/checkout-info/{orderId}", orderIdStr)
                        .header("X-MEMBER-ID", memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("100_abc"))
                .andExpect(jsonPath("$.orderAmount").value(14000))
                .andExpect(jsonPath("$.userPoint").value(50000))
                .andExpect(jsonPath("$.orderName").value("책주문"))
                .andExpect(jsonPath("$.ordererName").value("테스터"));
    }

    @Test
    @DisplayName("getCheckoutInfo - 주문 정보 없으면 404")
    void testGetCheckoutInfo_notFound() throws Exception {
        // GIVEN
        String orderIdStr = "999_abc";
        Long memberId = 10L;

        given(paymentService.getCheckoutInfo(orderIdStr, memberId))
                .willThrow(new PaymentNotFoundException("주문을 찾을 수 없습니다."));

        // WHEN & THEN
        mockMvc.perform(get("/task/payments/checkout-info/{orderId}", orderIdStr)
                        .header("X-MEMBER-ID", memberId))
                .andExpect(status().isNotFound());
    }

}
