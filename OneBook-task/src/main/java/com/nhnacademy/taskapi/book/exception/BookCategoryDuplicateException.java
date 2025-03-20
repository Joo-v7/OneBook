package com.nhnacademy.taskapi.book.exception;

public class BookCategoryDuplicateException extends RuntimeException {
    public BookCategoryDuplicateException(String message) {
        super(message);
    }
}
