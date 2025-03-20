package com.nhnacademy.taskapi.stock.service.Impl;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.exception.BookNotFoundException;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.order.dto.BookOrderRequest;
import com.nhnacademy.taskapi.stock.domain.Stock;
import com.nhnacademy.taskapi.stock.dto.StockCreateUpdateDTO;
import com.nhnacademy.taskapi.stock.exception.InvalidStockAmountException;
import com.nhnacademy.taskapi.stock.exception.StockDuplicateException;
import com.nhnacademy.taskapi.stock.exception.StockNotFoundException;
import com.nhnacademy.taskapi.stock.repository.StockRepository;
import com.nhnacademy.taskapi.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final BookRepository bookRepository;


    @Override
    @Transactional
    public Stock addStock(StockCreateUpdateDTO dto) {
        if(dto.getAmount() < 0){
            throw new InvalidStockAmountException("stock must be greater than 0 ");
        }

        Book book = bookRepository.findById(dto.getBookId()).orElseThrow(() -> new BookNotFoundException("Book not found"));

        Stock existStock = stockRepository.findByBook_bookId(dto.getBookId());

        if(Objects.nonNull(existStock)){
            throw new StockDuplicateException("The stock for the book already exists !");
        }

        Stock stock = new Stock();
        stock.setBook(book);
        stock.setStock(dto.getAmount());
        return stockRepository.save(stock);
    }

    @Override
    @Transactional
    public Stock updateStock(StockCreateUpdateDTO dto) {
        if(dto.getAmount() < 0){
            throw new InvalidStockAmountException("stock must be greater than 0 ");
        }
        Stock stock = stockRepository.findByBook_bookId(dto.getBookId());
        if(Objects.isNull(stock)){
            throw new StockNotFoundException("Stock not found");
        }

        stock.setStock(dto.getAmount());
        return stockRepository.save(stock);
    }

    @Override
    public Stock getStock(long bookId) {
        return stockRepository.findByBook_bookId(bookId);
    }

    public void orderUpdateStock(List<BookOrderRequest> bookOrderRequests) {
        for (BookOrderRequest bookOrderRequest : bookOrderRequests) {
            bookRepository.findById(bookOrderRequest.getBookId()).orElseThrow(() -> new BookNotFoundException("Book not found"));
            Stock stock = stockRepository.findByBook_bookId(bookOrderRequest.getBookId());

            int updateStock = stock.getStock() - bookOrderRequest.getQuantity();
            if (updateStock < 0) {
                throw new InvalidStockAmountException("재고가 부족합니다! 주문 수량을 변경해주세요.");
            }

            stock.setStock(updateStock);
        }
    }
}
