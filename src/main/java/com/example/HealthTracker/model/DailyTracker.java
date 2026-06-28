package com.example.HealthTracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_uname", "date"})
        }
)
@Data
@EntityListeners(AuditingEntityListener.class)
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


    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    @Override
    public String toString() {
        return "DailyTracker{" +
                "id=" + id +
                ", date=" + date +
                ", nutrients=" + nutrients +
                '}';
    }
}
