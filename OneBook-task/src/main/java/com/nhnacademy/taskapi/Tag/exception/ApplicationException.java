package com.nhnacademy.taskapi.Tag.exception;

import com.nhnacademy.taskapi.exception.dto.ErrorResponse;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    // ErrorResponse 반환 메서드
    private final ErrorResponse errorResponse;

    public ApplicationException(String title, int status) {
        super(title);
        this.errorResponse = new ErrorResponse(title, status);
    }
}