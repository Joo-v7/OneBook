package com.onebook.frontapi.feign.book;

import com.onebook.frontapi.dto.book.BookAuthorDTO;
import com.onebook.frontapi.dto.book.BookCategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "bookCategoryClient", url = "${onebook.gatewayUrl}")
public interface BookCategoryClient {

    @GetMapping("/task/book/category")
    Page<BookCategoryDTO> getAllBookCategories(@RequestParam int categoryId, Pageable pageable);

    @GetMapping("/task/book/category/{bookId}")
    BookCategoryDTO getBookCategory(@PathVariable Long bookId);
}