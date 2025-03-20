package com.nhnacademy.taskapi.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


// 리뷰 데이터를 서버에서 클라이언트로 응답할때 사용.
// 리뷰 조회 요청에 대한 응답
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private long reviewId;
    private int grade;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long memberId;
    private long bookId;
    private List<String> imageUrl;
    private String loginId;
}
