package com.example.HealthTracker.model.DTO;

import java.util.Date;

public record DailyTrackerData(
        Date date,
        Nutrients nutrients
) {
}
