package com.onebook.frontapi.dto.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyReviewResponseDto {
    private Long bookId;
    private String bookName;
    private String bookImageUrl;
    private LocalDateTime createdAt;
    private int grade;
    private String description;
    private Long reviewId;
}