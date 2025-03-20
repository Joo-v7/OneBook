package com.nhnacademy.taskapi.point.service;

import com.nhnacademy.taskapi.point.dto.PointLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointLogService {
    Page<PointLogResponse> findAllPointLogsByMemberId(Long member_id, Pageable pageable);
}