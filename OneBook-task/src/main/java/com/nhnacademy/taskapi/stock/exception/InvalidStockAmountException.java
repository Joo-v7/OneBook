package com.nhnacademy.taskapi.stock.exception;

public class InvalidStockAmountException extends RuntimeException {
    public InvalidStockAmountException(String message) {
        super(message);
    }
}
