package com.nhnacademy.taskapi.order.service;

import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.order.dto.BookOrderRequest;

import java.util.List;

public interface PricingService {
    int calculatorToTalPriceByOrderRequest(List<BookOrderRequest> items, BookRepository bookRepository);

    int calculatorDeliveryFee(List<BookOrderRequest> items);
}
