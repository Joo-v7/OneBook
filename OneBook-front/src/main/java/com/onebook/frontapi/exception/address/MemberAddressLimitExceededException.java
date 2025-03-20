package com.onebook.frontapi.exception.address;

public class MemberAddressLimitExceededException extends RuntimeException {
    public MemberAddressLimitExceededException(String message) {
        super(message);
    }
}
