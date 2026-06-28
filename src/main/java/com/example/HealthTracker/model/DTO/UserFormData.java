package com.example.HealthTracker.model.DTO;

import java.util.Date;

public record UserFormData (
        String uname,
        String password,
        String firstName,
        String lastName,
        String email,
        Date dateOfBirth,
        String gender,
        Integer height,
        Integer weight,
        String role
){
}
