package com.example.HealthTracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_uname", "date"})
        }
)
@Data
public class DailyTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Users user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private NutrientsInFlow nutrients;

    @Override
    public String toString() {
        return "DailyTracker{" +
                "id=" + id +
                ", date=" + date +
                ", nutrients=" + nutrients +
                '}';
    }
}
