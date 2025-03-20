package com.nhnacademy.taskapi.point.dto;

import com.nhnacademy.taskapi.point.domain.PointLogUpdatedType;
import com.nhnacademy.taskapi.point.domain.PointLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class PointLogResponse {
        private Long pointLogId;                 // 로그 PK
        private LocalDateTime pointLogUpdatedAt; // 포인트 변경일
        private String updatedType; // 포인트 변경 타입
        private int amount;                      // 포인트 갱신액
        private int currentPoint;                // 변경 후 현재 포인트
        private Long memberId;                   // 회원 식별자


        public static PointLogResponse fromEntity(PointLog pointLog) {
                return PointLogResponse.builder()
                        .pointLogId(pointLog.getPointLogId())
                        .pointLogUpdatedAt(pointLog.getPointLogUpdatedAt())
                        .updatedType(pointLog.getPointLogUpdatedType().getDescription())
                        .amount(pointLog.getPointLogAmount())
                        .currentPoint(pointLog.getAfterPointAmount())
                        .memberId(pointLog.getPoint().getMember().getId())
                        .build();
        }
}
