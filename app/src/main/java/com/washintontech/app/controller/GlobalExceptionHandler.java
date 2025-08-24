package com.washintontech.app.controller;

import com.washintontech.app.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler { // TODO: Add more specific exception handlers as needed
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(BadRequestException ex) {
        ErrorResponse error = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage())
                .title("Bad Request")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class) // fallback
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse error = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage())
                .title("Internal Server Error")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
