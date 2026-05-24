package com.example.HealthTracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Users {
    @Id
    private String uname;

    private String password;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private Integer height;
    private Integer weight;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DailyTracker>  dailyTrackers;
}
