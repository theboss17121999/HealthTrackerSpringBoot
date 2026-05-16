package com.example.HealthTracker.controller;


import com.example.HealthTracker.model.Users;
import com.example.HealthTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/users")
    public List<Users> getUsers() {
        List<Users> users = service.getUsers();
        return users;
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody Users user) {
        try {
            service.saveUser(user);
        }
        catch (Exception e){

            return new String("User already exists");
        }
        return user.toString();
    }

}
