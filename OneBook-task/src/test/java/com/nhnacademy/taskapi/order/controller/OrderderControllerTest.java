package com.nhnacademy.taskapi.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import com.nhnacademy.taskapi.order.dto.*;
import com.nhnacademy.taskapi.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testCreateOrder() throws Exception {

        // given
        Long memberId = 1L;
        // 테스트용 OrderFormRequest 객체 세팅
        OrderFormRequest orderFormRequest = new OrderFormRequest(); // OrderFormRequest 객체 설정

        // 1. BookOrderRequest 객체 세팅 (주문 항목)
        BookOrderRequest bookOrder1 = new BookOrderRequest();
        bookOrder1.setBookId(1L);  // 책 ID
        bookOrder1.setQuantity(2);  // 수량

        BookOrderRequest bookOrder2 = new BookOrderRequest();
        bookOrder2.setBookId(2L);  // 책 ID
        bookOrder2.setQuantity(1);  // 수량

        // 주문 항목 리스트 세팅
        orderFormRequest.setItems(Arrays.asList(bookOrder1, bookOrder2));

        // 2. DeliveryRequest 객체 세팅 (배송 정보)
        DeliveryRequest deliveryRequest = new DeliveryRequest();
        deliveryRequest.setRecipientAddress("서울시 강남구 테헤란로 123");
        deliveryRequest.setOrdererPhoneNumber("010-1234-5678");
        deliveryRequest.setOrdererName("홍길동");

        // 배송 정보 세팅
        orderFormRequest.setDelivery(deliveryRequest);

        // 3. Packaging ID 세팅
        orderFormRequest.setPackagingId(1);  // 포장 ID

        long newOrderId = 123L;
        when(orderService.processOrder(eq(memberId), any(OrderFormRequest.class))).thenReturn(newOrderId);

        // when
        mockMvc.perform(post("/task/order")  // URL 수정
                        .header("X-MEMBER-ID", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderFormRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(newOrderId)));

        // then
        verify(orderService, times(1)).processOrder(memberId, orderFormRequest);
    }

//    @Test
//    public void testGetOrders() throws Exception {
//        // given
//        Long memberId = 1L;
//        PageRequest pageable = PageRequest.of(0, 10);
//        List<OrderResponse> orderResponses = Arrays.asList(new OrderResponse(), new OrderResponse());  // 주문 응답 객체 설정
//        Page<OrderResponse> orderPage = new PageImpl<>(orderResponses, pageable, 2);
//
//        when(orderService.getOrderList(memberId, pageable)).thenReturn(orderPage);
//
//        // when
//        mockMvc.perform(get("/task/orders")
//                        .header("X-MEMBER-ID", memberId)
//                        .param("order-status", "")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content.length()").value(2));  // 응답의 content 배열 길이 확인
//
//        // then
//        verify(orderService, times(1)).getOrderList(memberId, pageable);
//    }

    @Test
    public void testGetOrdersByStatusName() throws Exception {
        // given
        String status = "결제대기";
        List<OrderStatusResponse> orderStatusResponses = Arrays.asList(new OrderStatusResponse(), new OrderStatusResponse());  // 주문 상태 응답 객체 설정

        when(orderService.getOrdersByStatusName(status)).thenReturn(orderStatusResponses);

        // when
        mockMvc.perform(get("/task/admin/orders")
                        .param("status", status)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));  // 응답 배열 길이 확인

        // then
        verify(orderService, times(1)).getOrdersByStatusName(status);
    }

    @Test
    public void testUpdateOrderStatus() throws Exception {
        // given
        List<Long> orderIds = Arrays.asList(1L, 2L);
        String status = "결제완료";

        // when
        mockMvc.perform(put("/task/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", status)
                        .content("[1, 2]"))  // 주문 ID 목록 전달
                .andExpect(status().isOk());

        // then
        verify(orderService, times(1)).updateOrderStatus(orderIds, status);  // 메서드 호출 확인
    }

    @Test
    public void testGetOrder() throws Exception {
        // given
        Long orderId = 1L;
        OrderMemberResponse orderMemberResponse = new OrderMemberResponse(

        );  // 주문 응답 객체 설정
        when(orderService.getOrder(orderId)).thenReturn(orderMemberResponse);

        // when
        mockMvc.perform(get("/task/orders/{order-id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        verify(orderService, times(1)).getOrder(orderId);
    }
}
