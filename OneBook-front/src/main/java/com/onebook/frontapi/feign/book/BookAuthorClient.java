package com.onebook.frontapi.feign.book;

import com.onebook.frontapi.dto.book.BookAuthorDTO;
import com.onebook.frontapi.dto.book.BookDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bookAuthorClient", url = "${onebook.gatewayUrl}")
public interface BookAuthorClient {



    @GetMapping("/task/book/author/{bookId}")
    public BookAuthorDTO getBookAuthor(@PathVariable("bookId") Long id);

}