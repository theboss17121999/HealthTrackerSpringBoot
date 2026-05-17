package com.example.HealthTracker.model.DTO;

import com.example.HealthTracker.model.DailyTracker;

import java.util.Date;
import java.util.List;

public record UserTrackerRecords(

        String uname,
        String email,
        String firstName,
        String lastName,
        Date dateOfBirth,
        String gender,
        Integer height,
        Integer weight,
        List<DailyTracker> dailyTrackers

) {
}