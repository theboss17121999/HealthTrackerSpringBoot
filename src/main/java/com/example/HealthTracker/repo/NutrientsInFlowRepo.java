package com.example.HealthTracker.repo;

import com.example.HealthTracker.model.NutrientsInFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NutrientsInFlowRepo extends JpaRepository<NutrientsInFlow, UUID> {
}
