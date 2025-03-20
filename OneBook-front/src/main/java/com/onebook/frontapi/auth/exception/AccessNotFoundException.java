package com.onebook.frontapi.auth.exception;

public class AccessNotFoundException extends RuntimeException {
    public AccessNotFoundException(String message) {
        super(message);
    }
}
