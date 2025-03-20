package com.nhnacademy.taskapi.book.controller;

import com.nhnacademy.taskapi.book.domain.BookTag;
import com.nhnacademy.taskapi.book.service.BookTagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task/book/tag")
@Tag(name = "Book", description = "도서를 생성, 수정, 삭제, 조회, 관리등 각종 도서 관련 기능")
public class BookTagController {
    private final BookTagService bookTagService;


    @GetMapping("/{bookId}")
    public ResponseEntity<BookTag> getBookTag(@PathVariable long bookId) {
        BookTag bookTag = bookTagService.getBookTag(bookId);
        return ResponseEntity.ok().body(bookTag);
    }
}
