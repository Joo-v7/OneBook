package com.nhnacademy.taskapi.review.service;

import com.nhnacademy.taskapi.review.dto.ReviewRequest;
import com.nhnacademy.taskapi.review.dto.ReviewResponse;
import org.springframework.data.domain.Page;

public interface ReviewService {
    ReviewResponse registerReview(long bookId, Long memberId, ReviewRequest reviewRequest); // 리뷰 등록

    double getReviewGradeAverage(long bookId); // 리뷰 평점 평균

    Page<ReviewResponse> getReviewByBook(long bookId, int page, int size);

    ReviewResponse updateReview(long bookId, long reviewId, Long memberId, ReviewRequest reviewRequest);

    ReviewResponse deleteReview(long bookId, long reviewId, Long memberId);
}
