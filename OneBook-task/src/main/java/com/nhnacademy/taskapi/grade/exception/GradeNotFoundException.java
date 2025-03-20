package com.nhnacademy.taskapi.grade.exception;


import jakarta.ws.rs.NotFoundException;

import java.util.NoSuchElementException;

public class GradeNotFoundException extends NoSuchElementException {
    public GradeNotFoundException(String message) {
        super(message);
    }
}
