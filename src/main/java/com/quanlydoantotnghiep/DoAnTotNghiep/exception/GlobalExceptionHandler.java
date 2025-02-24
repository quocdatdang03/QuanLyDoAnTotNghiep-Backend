package com.quanlydoantotnghiep.DoAnTotNghiep.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ErrorDetails> handleApiException(ApiException exception, WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false) // only get request URL, no other details
        );

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(errorDetails);
    }
}
