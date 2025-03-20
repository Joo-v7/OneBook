package com.nhnacademy.taskapi.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


// 내가 작성한 리뷰 모음
@AllArgsConstructor
@Data
public class MyReviewResponse {
    private Long bookId;
    private String bookName;
    private String bookImageUrl;
    private LocalDateTime createdAt;
    private int grade;
    private String description;
    private long reviewId;
}
