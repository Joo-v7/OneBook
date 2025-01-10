package com.onebook.frontapi.feign.book;

import com.onebook.frontapi.adaptor.packaging.PackagingResponseAdaptor;
import com.onebook.frontapi.dto.book.BookDTO;
import com.onebook.frontapi.dto.book.BookSaveDTO;
import com.onebook.frontapi.feign.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

//@FeignClient(name = "task-api", url = "http://localhost:8510")
@FeignClient(name = "bookClient", url = "http://localhost:8510", configuration = FeignConfig.class)
public interface BookClient {

    @GetMapping("/task/book/newbooks")
    Page<BookDTO> getNewBooks(@RequestParam("page") int page);

    @GetMapping("/task/book/{bookId}")
    BookDTO getBookById(@PathVariable("bookId") Long bookId);

    @PostMapping(value = "/task/book", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BookDTO createBook(@RequestPart(value = "dto") BookSaveDTO dto,
                       @RequestPart(value = "image") MultipartFile image);

}
