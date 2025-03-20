package com.onebook.frontapi.dto.review;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReviewResponseDto {
    private long reviewId;
    private int grade;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String memberId;
    private long bookId;
    private List<String> imageUrl;
    private String loginId;
}
