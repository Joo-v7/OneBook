package com.nhnacademy.taskapi.point.exception;

import com.nhnacademy.taskapi.exception.dto.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PointPolicyException extends ApplicationException {
    private final ErrorResponse errorResponse;

    // NOT_FOUND(404) - 포인트 정책을 찾을 수 없는 경우
    public static PointPolicyException pointPolicyNotFound() {
        return new PointPolicyException("Point policy not found", HttpStatus.NOT_FOUND);
    }

    // BAD_REQUEST(400) - 잘못된 요청
    public static PointPolicyException badRequest(String message) {
        return new PointPolicyException(message, HttpStatus.BAD_REQUEST);
    }

    // INTERNAL_SERVER_ERROR(500) - 서버 내부 오류
    public static PointPolicyException internalServerError() {
        return new PointPolicyException("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public PointPolicyException(String message, HttpStatus status) {
        super(message);  // 부모 클래스(ApplicationException)에서 메시지를 처리
        this.errorResponse = new ErrorResponse(message, status.value());
    }

    public HttpStatus getStatus() {
        return HttpStatus.valueOf(errorResponse.getStatus());
    }
}