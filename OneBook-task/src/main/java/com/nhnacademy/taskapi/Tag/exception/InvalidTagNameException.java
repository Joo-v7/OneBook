package com.nhnacademy.taskapi.Tag.exception;

public class InvalidTagNameException extends RuntimeException {
    public InvalidTagNameException(String message) {
        super(message);
    }
}