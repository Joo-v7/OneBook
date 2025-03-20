package com.nhnacademy.taskapi.review.controller;

import com.nhnacademy.taskapi.review.dto.BookReviewableResponse;
import com.nhnacademy.taskapi.review.dto.MyReviewResponse;
import com.nhnacademy.taskapi.review.dto.ReviewRequest;
import com.nhnacademy.taskapi.review.dto.ReviewResponse;
import com.nhnacademy.taskapi.review.service.MyReviewService;
import com.nhnacademy.taskapi.review.service.PendingReviewService;
import com.nhnacademy.taskapi.review.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 등록, 조회, 수정, 삭제 등 리뷰 관련 기능")
public class ReviewController {

    private final ReviewService reviewService;
    private final PendingReviewService pendingReviewService;
    private final MyReviewService myReviewService;

    // 리뷰 등록
    @PostMapping("/reviews/books/{bookId}")
    public ResponseEntity<ReviewResponse> registerReview(
            @PathVariable long bookId,
            @RequestHeader("X-MEMBER-ID") Long memberId,
            @Validated @RequestBody ReviewRequest reviewRequest) throws IOException {
        ReviewResponse response = reviewService.registerReview(bookId, memberId, reviewRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 도서의 리뷰 평점 평균
    @GetMapping("/reviews/books/{bookId}/average")
    public ResponseEntity<Double> getReviewGradeAverage(@PathVariable long bookId) {
        double average = reviewService.getReviewGradeAverage(bookId);
        return ResponseEntity.ok(average);
    }

    // 도서의 리뷰 목록 조회, 페이징 처리
    @GetMapping("/reviews/books/{bookId}")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByBook(
            @PathVariable long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ReviewResponse> reviews = reviewService.getReviewByBook(bookId, page, size);
        return ResponseEntity.ok(reviews);
    }

    // 작성된 리뷰 개수 확인
    @GetMapping("/reviews/count/books/{bookId}")
    public ResponseEntity<Integer> getReviewCount(@PathVariable Long bookId) {
        int reviewCount = reviewService.getReviewCount(bookId);
        return ResponseEntity.ok(reviewCount);
    }

    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}/books/{bookId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable long bookId,
            @PathVariable long reviewId,
            @RequestHeader("X-MEMBER-ID") Long memberId,
            @Validated @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse response = reviewService.updateReview(bookId, reviewId, memberId, reviewRequest);
        return ResponseEntity.ok(response);
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}/books/{bookId}")
    public ResponseEntity<ReviewResponse> deleteReview(
            @PathVariable long bookId,
            @PathVariable long reviewId,
            @RequestHeader("X-MEMBER-ID") Long memberId) {
        // 삭제 요청 시 별도의 ReviewRequest가 필요 없으므로, memberId만 전달
        ReviewResponse response = reviewService.deleteReview(bookId, reviewId, memberId);
        return ResponseEntity.ok(response);
    }

    // 리뷰를 작성하지 않은 도서
    @GetMapping("/members/pending-reviews")
    public ResponseEntity<List<BookReviewableResponse>> getPendingReviews(
            @RequestHeader("X-MEMBER-ID") long memberId
    ) {
        List<BookReviewableResponse> result = pendingReviewService.getPendingReviews(memberId);
        return ResponseEntity.ok(result);
    }

    // 리뷰를 작성한 도서
    @GetMapping("/members/my-reviews")
    public ResponseEntity<List<MyReviewResponse>> getMyReviews(
            @RequestHeader("X-MEMBER-ID") long memberId
    ) {
        List<MyReviewResponse> myReviews = myReviewService.getMyReviews(memberId);
        return ResponseEntity.ok(myReviews);
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(
            @RequestHeader("X-MEMBER-ID") long memberId,
            @PathVariable long reviewId) {
        ReviewResponse response = reviewService.getReviewById(memberId, reviewId);
        return ResponseEntity.ok(response);
    }
}
