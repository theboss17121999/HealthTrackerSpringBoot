package com.example.HealthTracker.repo;

import com.example.HealthTracker.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public interface UserRepo extends JpaRepository<Users, String> {

    boolean existsByEmail(String email);

//    Map<Object, Object> findByIdByDate(String id);
}
