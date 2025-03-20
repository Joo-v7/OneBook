package com.nhnacademy.taskapi.author.exception;

public class InvalidAuthorNameException extends RuntimeException {
    public InvalidAuthorNameException(String message) {
        super(message);
    }
}
