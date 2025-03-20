package com.nhnacademy.taskapi.book.exception;

public class BookTagDuplicateException extends RuntimeException {
    public BookTagDuplicateException(String message) {
        super(message);
    }
}
