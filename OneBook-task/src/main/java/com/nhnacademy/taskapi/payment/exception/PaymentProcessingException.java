package com.nhnacademy.taskapi.payment.exception;

public class PaymentProcessingException extends RuntimeException{

    public PaymentProcessingException(String message) {
        super(message);
    }

    public PaymentProcessingException(String message, Exception e) {
        super(message);
    }
}
