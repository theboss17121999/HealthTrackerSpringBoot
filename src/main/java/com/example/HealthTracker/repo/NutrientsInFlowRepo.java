package com.example.HealthTracker.repo;

import com.example.HealthTracker.model.DailyTracker;
import com.example.HealthTracker.model.NutrientsInFlow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NutrientsInFlowRepo extends JpaRepository<NutrientsInFlow, UUID> {
}
