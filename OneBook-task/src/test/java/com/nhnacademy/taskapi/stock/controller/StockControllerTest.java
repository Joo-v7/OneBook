package com.nhnacademy.taskapi.stock.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.stock.domain.Stock;
import com.nhnacademy.taskapi.stock.dto.StockCreateUpdateDTO;
import com.nhnacademy.taskapi.stock.service.StockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(StockController.class)
public class StockControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("재고 생성 테스트")
    void testCreateStock() throws Exception {
        // Mock 데이터 설정 (세터를 사용)
        long bookId = 1L;
        StockCreateUpdateDTO dto = new StockCreateUpdateDTO(bookId, 50);

        Book mockBook = new Book();
        mockBook.setBookId(bookId);
        mockBook.setTitle("Mock Book Title");


        Stock mockStock = new Stock();
        mockStock.setStockId(1L);
        mockStock.setBook(mockBook);
        mockStock.setStock(50);

        Mockito.when(stockService.addStock(any(StockCreateUpdateDTO.class))).thenReturn(mockStock);

        // 요청 및 검증
        mockMvc.perform(post("/task/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stockId").value(1L))
                .andExpect(jsonPath("$.book.bookId").value(bookId))
                .andExpect(jsonPath("$.stock").value(50));
    }

    @Test
    @DisplayName("재고 수정 테스트")
    void testModifyStock() throws Exception {
        // Mock 데이터 설정 (세터를 사용)
        long bookId = 1L;
        StockCreateUpdateDTO dto = new StockCreateUpdateDTO(bookId, 100);

        Book mockBook = new Book();
        mockBook.setBookId(bookId);
        mockBook.setTitle("Mock Book Title");


        Stock mockStock = new Stock();
        mockStock.setStockId(1L);
        mockStock.setBook(mockBook);
        mockStock.setStock(100);

        Mockito.when(stockService.updateStock(any(StockCreateUpdateDTO.class))).thenReturn(mockStock);

        // 요청 및 검증
        mockMvc.perform(put("/task/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockId").value(1L))
                .andExpect(jsonPath("$.book.bookId").value(bookId))
                .andExpect(jsonPath("$.stock").value(100));
    }
}
