package com.example.HealthTracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class NutrientsInFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Long fat;
    private Long protein;
    private Long carbohydrate;
    private Long fiber;
}
