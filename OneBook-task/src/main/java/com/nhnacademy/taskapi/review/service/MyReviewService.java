package com.nhnacademy.taskapi.review.service;

import com.nhnacademy.taskapi.review.dto.MyReviewResponse;

import java.util.List;

public interface MyReviewService {
    List<MyReviewResponse> getMyReviews(Long memberId);
}
