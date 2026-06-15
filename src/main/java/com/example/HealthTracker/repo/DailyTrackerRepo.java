package com.example.HealthTracker.repo;

import com.example.HealthTracker.model.DailyTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DailyTrackerRepo extends JpaRepository<DailyTracker, UUID> {

    @Query("SELECT d FROM DailyTracker d WHERE d.user.uname = :uname AND d.date = :date")
    Optional<DailyTracker> findByUnameAndDate(String uname, Date date);

    @Query("SELECT d FROM DailyTracker d WHERE d.user.uname = :uname")
    List<DailyTracker> findByUname(String uname);

    @Query("SELECT d FROM DailyTracker d WHERE d.user.uname = :id AND CAST(d.date AS date) = CAST(:date AS date)")
    Optional<DailyTracker> findByUserByDate(String id, Date date);
}
