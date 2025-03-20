package com.onebook.frontapi.feign.review;

import com.onebook.frontapi.dto.review.ReviewRequestDto;
import com.onebook.frontapi.dto.review.ReviewResponseDto;
import com.onebook.frontapi.dto.review.ReviewPageResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ReviewClient", url = "${onebook.gatewayUrl}")
public interface ReviewClient {

    /**
     * 리뷰 등록 (POST /task/books/{bookId}/reviews)
     */
    @PostMapping("/task//reviews/books/{bookId}")
    ReviewResponseDto registerReview(@PathVariable Long bookId,
                                     @RequestBody ReviewRequestDto reviewRequestDto);

    /**
     * 특정 도서의 리뷰 목록 페이징 조회 (GET /task/books/{bookId}/reviews?page=&size=)
     */
    @GetMapping("/task/reviews/books/{bookId}")
    ReviewPageResponseDto getReviews(@PathVariable Long bookId,
                                     @RequestParam(value = "page") int page,
                                     @RequestParam(value = "size") int size);

    /**
     * 특정 도서의 리뷰 평점 평균 (GET /task/books/{bookId}/reviews/average)
     */
    @GetMapping("/task/reviews/books/{bookId}/average")
    Double getReviewGradeAverage(@PathVariable("bookId") Long bookId);

    /**
     * 리뷰 수정 (PUT /task/books/{bookId}/reviews/{reviewId})
     */
    @PutMapping("/task/reviews/{reviewId}/books/{bookId}")
    ReviewResponseDto updateReview(@PathVariable("bookId") Long bookId,
                                   @PathVariable("reviewId") Long reviewId,
                                   @RequestBody ReviewRequestDto reviewRequestDto);

    /**
    * 특정 도서의 작성된 리뷰 개수
    **/
    @GetMapping("/task/reviews/count/books/{bookId}")
    int getReviewCount(@PathVariable("bookId") Long bookId);

    // 작성 된 리뷰 가져오기
    @GetMapping("/task/reviews/{reviewId}")
    ReviewResponseDto getReview(@PathVariable("reviewId") Long reviewId);

    // 리뷰 삭제 (DELETE) - 필요 시
    // @DeleteMapping("/task/books/{bookId}/reviews/{reviewId}")
    // ReviewResponseDto deleteReview(...);
}
