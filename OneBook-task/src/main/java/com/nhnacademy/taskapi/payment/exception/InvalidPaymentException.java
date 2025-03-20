package com.nhnacademy.taskapi.payment.exception;

public class InvalidPaymentException extends RuntimeException {

    public InvalidPaymentException(String message) {
        super(message);
    }

    public InvalidPaymentException(String message, Exception e) {
        super(message);
    }
}

