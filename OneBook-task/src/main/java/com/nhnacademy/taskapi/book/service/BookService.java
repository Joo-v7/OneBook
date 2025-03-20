package com.nhnacademy.taskapi.book.service;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    //책 등록
    Book saveBook(BookSaveDTO bookSaveDTO, MultipartFile imageFile);

    // 책 수정
    Book updateBook(Long bookId, BookUpdateDTO bookUpdateDTO);

    // 책 삭제
    void deleteBook(Long bookId);

    // 베스트셀러 목록 조회 (판매량 기준)
    Page<Book> bestSellerBooks(Pageable pageable);


    Page<Book> newBooks(Pageable pageable);

    List<Book> newBooksTop4();
    List<Book> bestSellersTop4();

    // 책 존재여부
    Book getBook(long bookId);

    Page<Book> findAllBooks(Pageable pageable);

    //통합검색
    List<BookSearchAllResponse> searchBookAll(String searchString);
    List<BookRecommendDto> recommendBooks();
}