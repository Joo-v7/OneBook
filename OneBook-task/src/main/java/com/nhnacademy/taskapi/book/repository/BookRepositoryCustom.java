package com.nhnacademy.taskapi.book.repository;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.dto.BookRecommendDto;
import com.nhnacademy.taskapi.book.dto.BookSearchAllResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookRepositoryCustom {
    List<BookSearchAllResponse> findBookByTitle(String searchString);
    List<BookRecommendDto> reconmmendBooks();
}