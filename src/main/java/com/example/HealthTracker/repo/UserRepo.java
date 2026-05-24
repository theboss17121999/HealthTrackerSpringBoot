package com.example.HealthTracker.repo;

import com.example.HealthTracker.model.DailyTracker;
import com.example.HealthTracker.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, String> {

    boolean existsByEmail(String email);

    Users findByUname(String uname);

//    Map<Object, Object> findByIdByDate(String id);
}
