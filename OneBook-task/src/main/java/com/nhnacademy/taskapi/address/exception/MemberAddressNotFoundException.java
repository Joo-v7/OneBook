package com.nhnacademy.taskapi.address.exception;

public class MemberAddressNotFoundException extends RuntimeException {
    public MemberAddressNotFoundException(String message) {
        super(message);
    }
}
