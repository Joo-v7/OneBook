package com.onebook.frontapi.dto.point.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PointLogResponse {
    private Long pointLogId;
    private LocalDateTime pointLogUpdatedAt;
    private String updatedType;
    private int amount;
    private int currentPoint;
    private Long memberId;
}
