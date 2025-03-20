package com.nhnacademy.taskapi.publisher.exception;

import jakarta.ws.rs.NotFoundException;

public class PublisherNotFoundException extends RuntimeException {
    public PublisherNotFoundException(String message) {
        super(message);
    }
}
