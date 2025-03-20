package com.nhnacademy.taskapi.book.controller;


import com.nhnacademy.taskapi.book.domain.BookAuthor;
import com.nhnacademy.taskapi.book.dto.BookAuthorCreateDTO;
import com.nhnacademy.taskapi.book.service.BookAuthorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task/book/author")
@Tag(name = "Book", description = "도서를 생성, 수정, 삭제, 조회, 관리등 각종 도서 관련 기능")  // API 그룹 설명 추가
@Slf4j
public class BookAuthorController {
    private final BookAuthorService bookAuthorService;

    @PostMapping
    public ResponseEntity<?> createBookAuthor(@RequestBody @Valid BookAuthorCreateDTO bookAuthorCreateDTO) {
        BookAuthor bookAuthor = bookAuthorService.createBookAuthor(bookAuthorCreateDTO);
        return ResponseEntity.ok().body(bookAuthor);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookAuthor> getBookAuthor(@PathVariable("bookId") Long bookId) {
        BookAuthor bookAuthor = bookAuthorService.getBookAuthorByBookId(bookId);
        return ResponseEntity.ok().body(bookAuthor);
    }
}
