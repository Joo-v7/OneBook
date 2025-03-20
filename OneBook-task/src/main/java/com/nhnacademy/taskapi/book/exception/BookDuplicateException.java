package com.nhnacademy.taskapi.book.exception;

public class BookDuplicateException extends RuntimeException {
    public BookDuplicateException(String message) {
        super(message);
    }
}
