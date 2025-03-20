package com.nhnacademy.taskapi.payment.exception;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException(String message, Exception e) {
        super(message);
    }
}
