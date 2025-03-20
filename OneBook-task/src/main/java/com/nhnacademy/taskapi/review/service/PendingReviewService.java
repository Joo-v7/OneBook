package com.nhnacademy.taskapi.review.service;

import com.nhnacademy.taskapi.review.dto.BookReviewableResponse;

import java.util.List;

// 리뷰 작성이 안된 도서를 찾기위한 서비스
public interface PendingReviewService {

    List<BookReviewableResponse> getPendingReviews(Long memberId);
}
