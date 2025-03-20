package com.nhnacademy.taskapi.point.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

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


    public static PointLogPageResponseDto fromPage(Page<PointLogResponse> page) {
        PointLogPageResponseDto dto = new PointLogPageResponseDto();
        dto.setContent(page.getContent());
        dto.setTotalPages(page.getTotalPages());
        dto.setTotalElements(page.getTotalElements());
        dto.setPageNumber(page.getNumber());
        dto.setSize(page.getSize());
        dto.setFirst(page.isFirst());
        dto.setLast(page.isLast());
        return dto;
    }
}
