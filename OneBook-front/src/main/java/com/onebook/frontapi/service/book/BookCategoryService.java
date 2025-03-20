package com.onebook.frontapi.service.book;

import com.onebook.frontapi.dto.book.BookCategoryDTO;
import com.onebook.frontapi.feign.book.BookCategoryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCategoryService {
    private final BookCategoryClient bookCategoryClient;

    public Page<BookCategoryDTO> getAllBookCategories(int categoryId, Pageable pageable) {
        return bookCategoryClient.getAllBookCategories(categoryId, pageable);
    }

    public BookCategoryDTO getBookCategoryByBookId(long bookId) {
        return bookCategoryClient.getBookCategory(bookId);
    }
}
