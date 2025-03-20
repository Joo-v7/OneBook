package com.onebook.frontapi.controller.review;

import com.onebook.frontapi.dto.review.ReviewPageResponseDto;
import com.onebook.frontapi.dto.review.ReviewRequestDto;
import com.onebook.frontapi.dto.review.ReviewResponseDto;
import com.onebook.frontapi.feign.review.ReviewClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/front/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewFrontController {

    private final ReviewClient reviewClient;

    // 리뷰 등록
    @PostMapping("/{bookId}")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable Long bookId,
            @Validated @RequestBody ReviewRequestDto requestDto
    ) {
        log.info("Creating review for bookId: {}", bookId);
        ReviewResponseDto response = reviewClient.registerReview(bookId, requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 특정 도서 리뷰 조회 (페이지네이션)
    @GetMapping("/{bookId}")
    public ResponseEntity<ReviewPageResponseDto> getReviews(
            @PathVariable Long bookId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        ReviewPageResponseDto pageResponse = reviewClient.getReviews(bookId, page, size);
        return ResponseEntity.ok(pageResponse);
    }

    // 리뷰 수정
    @PutMapping("/{bookId}/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long bookId,
            @PathVariable Long reviewId,
            @Validated @RequestBody ReviewRequestDto requestDto
    ) {
        ReviewResponseDto updated = reviewClient.updateReview(bookId, reviewId, requestDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{bookId}/average")
    public ResponseEntity<Double> getReviewAverage(@PathVariable Long bookId) {
        Double averageGrade = reviewClient.getReviewGradeAverage(bookId);
        return ResponseEntity.ok(averageGrade);
    }

    @GetMapping("/{bookId}/count")
    public ResponseEntity<Integer> getReviewCount(@PathVariable Long bookId) {
        int reviewCount = reviewClient.getReviewCount(bookId);
        return ResponseEntity.ok(reviewCount);
    }
    // 리뷰 삭제 (필요 시 추가)
}
