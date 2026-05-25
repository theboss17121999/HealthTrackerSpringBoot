package com.example.HealthTracker.controller;


import com.example.HealthTracker.model.DTO.DailyTrackerData;
import com.example.HealthTracker.model.DTO.LoginRequest;
import com.example.HealthTracker.model.DTO.UserTrackerRecords;
import com.example.HealthTracker.model.DailyTracker;
import com.example.HealthTracker.model.Users;
import com.example.HealthTracker.service.JwtService;
import com.example.HealthTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/users")
    public List<UserTrackerRecords> getUsers() {
        List<UserTrackerRecords> users = service.getUsers();
        return users;
    }

    @PreAuthorize("#id == authentication.name")
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
    @PreAuthorize("#id == authentication.name")
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
    @PreAuthorize("#id == authentication.name")
    public HttpStatus editDailyTracker(@PathVariable
                                        String id,
                                        @PathVariable
                                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                                        Date date,
                                        @RequestBody DailyTracker dailyTracker) {

        return service.editDailyTracker(id,date,dailyTracker);
//        return service.getUsers().toString();
    }

    @DeleteMapping("/deleteUser/{id}")
//    @PreAuthorize("#id == authentication.name")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {

        try {
            service.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("DailyTracker/{id}")
    @PreAuthorize("#id == authentication.name")
    public ResponseEntity<?> getDailyTrackerById(@PathVariable String id) {
        try {
            List<DailyTrackerData> data = service.trackerRecords(id);
            return ResponseEntity.ok(data);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest login) {

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    login.uname(),
                                    login.password()
                            )
                    );

            if(authentication.isAuthenticated()) {
                return jwtService.generateToken(login.uname());
            }

        } catch (Exception e) {
            return "Login Failed Please try again";
        }

        return "Login Failed";
    }

}
