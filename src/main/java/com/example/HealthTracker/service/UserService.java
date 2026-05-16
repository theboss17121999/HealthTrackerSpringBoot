package com.example.HealthTracker.service;

import com.example.HealthTracker.model.Users;
import com.example.HealthTracker.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    public List<Users> getUsers() {
        return userRepo.findAll();
    }

    public void saveUser(Users user) throws Exception{
        userRepo.save(user);
    }
}
