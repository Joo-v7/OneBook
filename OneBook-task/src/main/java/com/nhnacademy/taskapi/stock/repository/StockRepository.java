package com.nhnacademy.taskapi.stock.repository;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByBook_bookId(Long bookId);
    Optional<Stock> findByBook(Book book);
    Stock findByBook_BookId(Long bookId);
}
