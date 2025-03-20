package com.nhnacademy.taskapi.stock.exception;

public class StockDuplicateException extends RuntimeException {
    public StockDuplicateException(String message) {
        super(message);
    }
}
