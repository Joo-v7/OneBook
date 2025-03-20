package com.onebook.frontapi.dto.point.response;

import lombok.Data;

import java.util.List;

@Data
public class PointLogPageResponseDto {
    private List<PointLogResponse> content;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int size;
    private boolean first;
    private boolean last;
}

