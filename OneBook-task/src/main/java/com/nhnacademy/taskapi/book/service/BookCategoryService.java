package com.nhnacademy.taskapi.book.service;

import com.nhnacademy.taskapi.book.domain.BookCategory;
import com.nhnacademy.taskapi.book.dto.BookCategorySaveDTO;
import com.nhnacademy.taskapi.category.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookCategoryService {
    BookCategory save(BookCategorySaveDTO bookCategorySaveDTO);
    Page<BookCategory> getBookByCategory(int categoryId, Pageable pageable);
    BookCategory getBookCategoryByBookId(long bookId);
}
