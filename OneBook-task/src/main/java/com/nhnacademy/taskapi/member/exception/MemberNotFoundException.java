package com.nhnacademy.taskapi.member.exception;


import jakarta.ws.rs.NotFoundException;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
