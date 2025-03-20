package com.nhnacademy.taskapi.address.exception;

public class MemberAddressLimitExceededException extends RuntimeException {
    public MemberAddressLimitExceededException(String message) {
        super(message);
    }
}
