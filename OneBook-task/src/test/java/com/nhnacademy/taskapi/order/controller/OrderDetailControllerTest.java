package com.nhnacademy.taskapi.order.controller;

import com.nhnacademy.taskapi.order.dto.OrderDetailResponse;
import com.nhnacademy.taskapi.order.exception.OrderNotFoundException;
import com.nhnacademy.taskapi.order.service.OrderDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderDetailControllerTest {

    @InjectMocks
    private OrderDetailController orderDetailController;

    @Mock
    private OrderDetailService orderDetailService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderDetailController)
                .build();
    }

    @Test
    @DisplayName("주문 상세 정보 조회 성공 테스트")
    void getOrderDetail_Success() throws Exception {
        // given
        Long memberId = 1L;
        Long orderId = 100L;
        OrderDetailResponse mockResponse = new OrderDetailResponse(); // 필요한 데이터 세팅

        given(orderDetailService.getOrderDetail(anyLong(), anyLong()))
                .willReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/task/order-detail/{orderId}", orderId)
                        .header("X-MEMBER-ID", memberId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderDetailService).getOrderDetail(memberId, orderId);
    }

    @Test
    @DisplayName("memberId 헤더 누락 시 실패 테스트")
    void getOrderDetail_WithoutMemberId_Fail() throws Exception {
        // given
        Long orderId = 100L;

        // when & then
        mockMvc.perform(get("/task/order-detail/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 orderId 형식 테스트")
    void getOrderDetail_InvalidOrderId_Fail() throws Exception {
        // given
        Long memberId = 1L;

        // when & then
        mockMvc.perform(get("/task/order-detail/invalid")
                        .header("X-MEMBER-ID", memberId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // TODO 물어보기
//    @Test
//    @DisplayName("서비스 예외 발생 테스트")
//    void getOrderDetail_ServiceException() throws Exception {
//        // given
//        Long memberId = 1L;
//        Long orderId = 100L;
//
//        given(orderDetailService.getOrderDetail(anyLong(), anyLong()))
//                .willThrow(new OrderNotFoundException(""));
//
//        // when & then
//        mockMvc.perform(get("/task/order-detail/{orderId}", orderId)
//                        .header("X-MEMBER-ID", memberId)
//                        .contentType(MediaType.APPLICATION_JSON))
////                .andExpect(status().isInternalServerError());
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Order not found"));
//
//    }
}