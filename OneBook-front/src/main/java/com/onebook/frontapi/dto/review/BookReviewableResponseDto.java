package com.onebook.frontapi.dto.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookReviewableResponseDto {
    private Long bookId;
    private String bookName;
    private String bookImageUrl;
    private LocalDateTime purchasedAt;
}
