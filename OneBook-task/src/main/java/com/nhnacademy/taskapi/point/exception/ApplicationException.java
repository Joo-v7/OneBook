package com.nhnacademy.taskapi.point.exception;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String message) {
        super(message);  // 부모 클래스에서 메시지를 처리
    }
}
