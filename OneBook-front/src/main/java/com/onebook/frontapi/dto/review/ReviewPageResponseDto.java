package com.onebook.frontapi.dto.review;

import lombok.Data;

import java.util.List;

@Data
public class ReviewPageResponseDto {
    private List<ReviewResponseDto> content;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
}
