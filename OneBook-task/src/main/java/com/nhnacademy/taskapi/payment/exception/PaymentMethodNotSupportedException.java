package com.nhnacademy.taskapi.payment.exception;

public class PaymentMethodNotSupportedException extends RuntimeException{

    public PaymentMethodNotSupportedException(String message) {
        super(message);
    }

    public PaymentMethodNotSupportedException(String message, Exception e) {
        super(message);
    }
}
