package com.onebook.frontapi.feign.book;

import com.onebook.frontapi.adaptor.packaging.PackagingResponseAdaptor;
import com.onebook.frontapi.dto.book.*;
import com.onebook.frontapi.feign.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@FeignClient(name = "bookClient", url = "${onebook.gatewayUrl}", configuration = FeignConfig.class)
public interface BookClient {

    @GetMapping("/task/book/newbooks")
    Page<BookDTO> getNewBooks(Pageable pageable);

    @GetMapping("/task/book/{bookId}")
    BookDTO getBookById(@PathVariable("bookId") Long bookId);

    @PostMapping(value = "/task/book", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BookDTO createBook(@RequestPart(value = "dto") BookSaveDTO dto,
                       @RequestPart(value = "image") MultipartFile image);


    @GetMapping("/task/book/book-list")
    Page<BookDTO> getAllBooks(Pageable pageable);

    @PutMapping("/task/book/{bookId}")
    BookDTO updateBook(@PathVariable("bookId") long bookId, @RequestBody BookUpdateDTO dto);

    @DeleteMapping("/task/book/{bookId}")
    void deleteBook(@PathVariable("bookId") long bookId);

    //알라딘
    @PostMapping("/task/book/aladin")
    String aladinBook();

    @GetMapping("/task/book/newbooks/top4")
    List<BookDTO> getTop4Books();

    @GetMapping("/task/book/bestsellers/top4")
    List<BookDTO> getBestSellersTop4();

    @GetMapping("/task/book/bestsellers")
    Page<BookDTO> getBestSellers(Pageable pageable);

    @GetMapping("/task/book/search/all")
    List<BookSearchAllResponse> searchBookAll(@RequestParam("searchString") String searchString);

    @GetMapping("/task/book/recommend")
    List<BookRecommendDto> recommendBook();
}
