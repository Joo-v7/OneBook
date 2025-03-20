package com.nhnacademy.taskapi.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


// 리뷰 미작성 도서 목록
@Data
@AllArgsConstructor
public class BookReviewableResponse {
    private Long bookId;
    private String bookName;
    private String bookImageUrl;
    private LocalDateTime purchasedAt;
}
