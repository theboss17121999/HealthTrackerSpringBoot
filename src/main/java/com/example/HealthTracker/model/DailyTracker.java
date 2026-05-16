package com.example.HealthTracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class DailyTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_uname")
    private Users user;

    @OneToOne(cascade = CascadeType.ALL)
    private NutrientsInFlow nutrients;
}
