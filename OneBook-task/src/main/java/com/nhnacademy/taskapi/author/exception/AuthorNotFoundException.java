package com.nhnacademy.taskapi.author.exception;

import jakarta.ws.rs.NotFoundException;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {super(message);}
}
