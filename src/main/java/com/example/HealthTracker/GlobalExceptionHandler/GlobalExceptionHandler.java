package com.example.HealthTracker.GlobalExceptionHandler;

import org.springframework.core.task.TaskRejectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.ExecutionException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TaskRejectedException.class)
    public ResponseEntity<String> handleTaskRejected(TaskRejectedException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE) // 503
                .body("Server is busy. Please try again later.");
    }

    @ExceptionHandler(ExecutionException.class)
    public ResponseEntity<String> handleException(ExecutionException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE) // 503
                .body(ex.getMessage());
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<String> handleArithmeticException(ArithmeticException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ex.getMessage());
    }
}
