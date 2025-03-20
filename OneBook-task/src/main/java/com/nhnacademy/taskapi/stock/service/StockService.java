package com.nhnacademy.taskapi.stock.service;

import com.nhnacademy.taskapi.order.dto.BookOrderRequest;
import com.nhnacademy.taskapi.stock.domain.Stock;
import com.nhnacademy.taskapi.stock.dto.StockCreateUpdateDTO;

import java.util.List;

public interface StockService {
    Stock addStock(StockCreateUpdateDTO dto);
    Stock updateStock(StockCreateUpdateDTO dto);
    Stock getStock(long bookId);
    void orderUpdateStock(List<BookOrderRequest> bookOrderRequests);
}
