package com.nhnacademy.taskapi.category.exception;

public class CategoryNameDuplicateException extends RuntimeException {
  public CategoryNameDuplicateException(String message) {
    super(message);
  }
}
