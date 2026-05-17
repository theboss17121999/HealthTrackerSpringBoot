package com.example.HealthTracker.controller;


import com.example.HealthTracker.model.DailyTracker;
import com.example.HealthTracker.model.Users;
import com.example.HealthTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/users")
    public List<Users> getUsers() {
        List<Users> users = service.getUsers();
        return users;
    }

    @GetMapping("/users/{id}")
    public Optional<Users> getUsersById(@PathVariable String id) {
        Optional<Users> users = service.getUsers(id);
        return users;
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody Users user) {
        try {
            switch (service.saveUser(user)){
                case CREATED: return "User saved successfully";
                case CONFLICT: return "Username or email already exists";
                default: return "User";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
//        return new String();y
    }

    @PostMapping("/addDailyProgress/{id}")
    public String addDailyTracker(@PathVariable String id,
                                  @RequestBody DailyTracker dailyTracker) {

        try {
            switch (service.addDailyTracker(id, dailyTracker )){
                case CREATED: return "Record has been Added Successfully";
                case CONFLICT: return "Record is already Exists for this date";
                default: return "User";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
