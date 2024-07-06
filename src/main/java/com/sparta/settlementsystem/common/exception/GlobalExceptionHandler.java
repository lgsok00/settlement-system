package com.sparta.settlementsystem.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<String> handleCustomException(CustomException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An errror occurred");
  }
}
