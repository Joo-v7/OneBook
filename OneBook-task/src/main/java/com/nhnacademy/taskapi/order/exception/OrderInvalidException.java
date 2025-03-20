package com.nhnacademy.taskapi.order.exception;

public class OrderInvalidException extends RuntimeException {
    public OrderInvalidException(String s) {
        super(s);
    }
}
