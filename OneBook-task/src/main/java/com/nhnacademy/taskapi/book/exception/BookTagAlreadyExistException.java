package com.nhnacademy.taskapi.book.exception;

public class BookTagAlreadyExistException extends RuntimeException {
    public BookTagAlreadyExistException(String message) {
        super(message);
    }
}
