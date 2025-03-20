package com.nhnacademy.taskapi.book.controller;


import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.dto.BookRecommendDto;
import com.nhnacademy.taskapi.book.dto.BookSaveDTO;
import com.nhnacademy.taskapi.book.dto.BookSearchAllResponse;
import com.nhnacademy.taskapi.book.dto.BookUpdateDTO;
import com.nhnacademy.taskapi.book.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task/book")
@Slf4j
@Tag(name = "Book", description = "도서를 생성, 수정, 삭제, 조회, 관리등 각종 도서 관련 기능")
public class BookController {
    private final BookService bookService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Book> addBook(@RequestPart(value = "dto") BookSaveDTO dto,
                                        @RequestPart(value = "image") MultipartFile image){
        Book book = bookService.saveBook(dto, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @PutMapping("{bookId}")
    public ResponseEntity<Book> modifyBook(@PathVariable long bookId, @RequestBody BookUpdateDTO dto){
        Book book = bookService.updateBook(bookId, dto);
        return ResponseEntity.ok().body(book);
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable long bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();    }


    @GetMapping("/bestsellers")
    public ResponseEntity<Page<Book>> bestsellersBook(Pageable pageable){
        Page<Book> books = bookService.bestSellerBooks(pageable);
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/newbooks")
    public ResponseEntity<Page<Book>> newbooksBook(Pageable pageable){
        Page<Book> books = bookService.newBooks(pageable);
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/newbooks/top4")
    public ResponseEntity<List<Book>> top4Books(){
        List<Book> books = bookService.newBooksTop4();
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/bestsellers/top4")
    public ResponseEntity<List<Book>> bestsellersTop4(){
        List<Book> books = bookService.bestSellersTop4();
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable long bookId){
        Book book = bookService.getBook(bookId);
        return ResponseEntity.ok().body(book);
    }

    @GetMapping("/book-list")
    public ResponseEntity<Page<Book>> getBookList(Pageable pageable){
        Page<Book> books = bookService.findAllBooks(pageable);
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/search/all")
    public ResponseEntity<List<BookSearchAllResponse>> searchBooks(@RequestParam("searchString") String searchString){
        List<BookSearchAllResponse> books = bookService.searchBookAll(searchString);
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<BookRecommendDto>> recommendBooks(){
        return ResponseEntity.ok().body(bookService.recommendBooks());
    }

}