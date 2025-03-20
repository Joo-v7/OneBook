package com.onebook.frontapi.feign.book;

import com.onebook.frontapi.dto.book.BookCategoryDTO;
import com.onebook.frontapi.dto.book.BookTagDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bookTagClient", url = "${onebook.gatewayUrl}")
public interface BookTagClient {
    @GetMapping("/task/book/tag/{bookId}")
    BookTagDTO getBookTagByBookId(@PathVariable("bookId") Long bookId);
}