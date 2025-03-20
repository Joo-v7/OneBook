package com.nhnacademy.taskapi.order.controller;

import com.nhnacademy.taskapi.order.service.OrderStatusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = OrderStatusController.class)
class OrderStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderStatusService orderStatusService;

    @Test
    @DisplayName("GET /task/order/statuses - 성공 케이스")
    void getOrderStatuses_Success() throws Exception {
        // Mock 데이터 설정
        List<String> mockStatuses = Arrays.asList("Pending", "Processing", "Completed");
        given(orderStatusService.getOrderStatusesList()).willReturn(mockStatuses);

        // MockMvc를 통해 테스트 요청
        mockMvc.perform(get("/task/order/statuses")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 상태 코드 200 OK 검증
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Content-Type 검증
                .andExpect(jsonPath("$.length()").value(mockStatuses.size())) // 응답 배열 크기 검증
                .andExpect(jsonPath("$[0]").value("Pending")) // 첫 번째 항목 검증
                .andExpect(jsonPath("$[1]").value("Processing")) // 두 번째 항목 검증
                .andExpect(jsonPath("$[2]").value("Completed")); // 세 번째 항목 검증
    }
}
