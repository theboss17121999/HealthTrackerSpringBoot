package com.example.HealthTracker.exception;

import org.springframework.core.task.TaskRejectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TaskRejectedException.class)
    public ResponseEntity<String> handleTaskRejected(TaskRejectedException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE) // 503
                .body("Server is busy. Please try again later.");
    }
}
