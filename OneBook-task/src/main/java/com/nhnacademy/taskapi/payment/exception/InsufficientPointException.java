package com.nhnacademy.taskapi.payment.exception;

public class InsufficientPointException extends RuntimeException{

    public InsufficientPointException(String message) {
        super(message);
    }

    public InsufficientPointException(String message, Exception e) {
        super(message);
    }
}
