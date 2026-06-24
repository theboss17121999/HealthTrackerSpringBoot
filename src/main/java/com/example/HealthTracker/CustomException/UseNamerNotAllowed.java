package com.example.HealthTracker.CustomException;

public class UseNamerNotAllowed extends RuntimeException {

    public UseNamerNotAllowed(String message) {
        super(message);
    }
}
