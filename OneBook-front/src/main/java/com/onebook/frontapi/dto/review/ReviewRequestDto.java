package com.onebook.frontapi.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ReviewRequestDto {

    @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5 이하여야 합니다.")
    private int grade;

    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String description;

    private List<String> imageUrl; // 최대 3장
}
