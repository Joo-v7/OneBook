package com.nhnacademy.taskapi.point.controller;

import com.nhnacademy.taskapi.point.domain.Point;
import com.nhnacademy.taskapi.point.domain.PointLog;
import com.nhnacademy.taskapi.point.dto.PointLogPageResponseDto;
import com.nhnacademy.taskapi.point.dto.PointLogResponse;
import com.nhnacademy.taskapi.point.service.PointLogService;
import com.nhnacademy.taskapi.member.domain.Member;

import com.nhnacademy.taskapi.point.service.PointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task/member")
@Tag(name = "PointLog", description = "포인트 사용기록과 사용자의 현재 포인트를 조회")
public class PointLogController {

    private final PointLogService pointLogService;
    private final PointService pointService;

    @GetMapping("/point-logs")
    public ResponseEntity<PointLogPageResponseDto> getPointLogs(
            @RequestHeader("X-MEMBER-ID") Long memberId,
            Pageable pageable) {

        Page<PointLogResponse> logs = pointLogService.findAllPointLogsByMemberId(memberId, pageable);

        PointLogPageResponseDto responseDto = PointLogPageResponseDto.fromPage(logs);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/current-point")
    public int getUserPoint(
            @RequestHeader("X-MEMBER-ID") Long memberId) {

        Point point = pointService.getMemberPoints(memberId);

        return point.getPointCurrent();
    }


}