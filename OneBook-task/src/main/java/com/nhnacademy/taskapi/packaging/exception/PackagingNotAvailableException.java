package com.nhnacademy.taskapi.packaging.exception;

public class PackagingNotAvailableException extends RuntimeException {
    public PackagingNotAvailableException() {
        super("Packaging is not available");
    }
}
