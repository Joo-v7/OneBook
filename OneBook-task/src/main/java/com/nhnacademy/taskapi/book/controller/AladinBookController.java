package com.nhnacademy.taskapi.book.controller;

import com.nhnacademy.taskapi.book.service.Impl.AladinBookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task/book/aladin")
@Tag(name = "AladinBook", description = "알라딘 api로부터 책들을 받아와 저장")  // API 그룹 설명 추가
public class AladinBookController {
    private final AladinBookService aladinBookService;

    @PostMapping
    public ResponseEntity<String> saveBooksFromAladin() throws IOException {

        aladinBookService.saveBookFromAladin();
        return ResponseEntity.status(HttpStatus.CREATED).body("Books saved successfully.");

    }
}
