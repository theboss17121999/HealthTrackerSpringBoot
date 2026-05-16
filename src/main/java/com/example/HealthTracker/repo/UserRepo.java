package com.example.HealthTracker.repo;

import com.example.HealthTracker.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, Integer> {

}
