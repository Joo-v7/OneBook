package com.nhnacademy.taskapi.packaging.exception;

public class PackagingAlreadyExistException extends RuntimeException {
    public PackagingAlreadyExistException(String message) {
        super(message);
    }
}
