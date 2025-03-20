package com.nhnacademy.taskapi.book.exception;

public class BookAuthorAlreadyExistsException extends RuntimeException {
    public BookAuthorAlreadyExistsException(String message) {
        super(message);
    }
}
