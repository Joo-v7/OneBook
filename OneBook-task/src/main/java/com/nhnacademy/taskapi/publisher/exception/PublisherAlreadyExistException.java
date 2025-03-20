package com.nhnacademy.taskapi.publisher.exception;

public class PublisherAlreadyExistException extends RuntimeException {
    public PublisherAlreadyExistException(String message) {
        super(message);
    }
}
