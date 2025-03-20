package com.nhnacademy.taskapi.point.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "point_logs")
public class PointLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointLogId;

    @NotNull(message = "포인트 변경 날짜는 필수입니다.")
    @Column(nullable = false)
    private LocalDateTime pointLogUpdatedAt;

    // pointLogUpdatedType을 String 대신 Enum으로 변경
    @NotNull(message = "포인트 변경 타입은 필수입니다.")
    @Enumerated(EnumType.STRING)  // Enum 값을 저장할 때 String 형태로 저장
    @Column(nullable = false, length = 20)
    private PointLogUpdatedType pointLogUpdatedType;

    @NotNull(message = "포인트 갱신액은 필수입니다.")
    @Column(nullable = false, precision = 10, scale = 2)
    private int pointLogAmount;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "point_id")
    private Point point;

    // 변경 후 포인트를 나타내기 위해 새로 추가함
    private int afterPointAmount;

    @Builder
    public PointLog(Long pointLogId, LocalDateTime pointLogUpdatedAt, PointLogUpdatedType pointLogUpdatedType, int pointLogAmount, Point point, int afterPointAmount) {
        this.pointLogId = pointLogId;
        this.pointLogUpdatedAt = pointLogUpdatedAt;
        this.pointLogUpdatedType = pointLogUpdatedType;
        this.pointLogAmount = pointLogAmount;
        this.point = point;
        this.afterPointAmount = afterPointAmount;
    }
}
