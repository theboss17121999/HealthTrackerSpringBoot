package com.example.HealthTracker.controller;


import com.example.HealthTracker.model.DTO.UserTrackerRecords;
import com.example.HealthTracker.model.DailyTracker;
import com.example.HealthTracker.model.Users;
import com.example.HealthTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
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
    public UserTrackerRecords getUsersById(@PathVariable String id) {
        Optional<UserTrackerRecords> users = service.getUsers(id);
        if (users.isPresent()) {
            return users.get();
        }
        System.out.println("User not found");
        return null;

    }

    @PostMapping("/register")
    public String addUser(@RequestBody Users user) {
        try {
            return switch (service.saveUser(user)) {
                case CREATED -> "User saved successfully";
                case CONFLICT -> "Username or email already exists";
                default -> "User";
            };
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

    @PutMapping("editDailyProgress/{id}/{date}")
    public HttpStatus editDailyTracker(@PathVariable
                                        String id,
                                        @PathVariable
                                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                                        Date date,
                                        @RequestBody DailyTracker dailyTracker) {

        return service.editDailyTracker(id,date,dailyTracker);
//        return service.getUsers().toString();
    }

}
