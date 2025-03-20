package com.nhnacademy.taskapi.roles.exception;

import jakarta.ws.rs.NotFoundException;

import java.util.NoSuchElementException;

public class RoleNotFoundException extends NoSuchElementException {
  public RoleNotFoundException(String message) {
    super(message);
  }
}
