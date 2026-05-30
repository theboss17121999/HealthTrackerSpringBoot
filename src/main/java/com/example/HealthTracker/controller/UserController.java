package com.example.HealthTracker.controller;


import com.example.HealthTracker.model.DTO.DailyTrackerData;
import com.example.HealthTracker.model.DTO.LoginRequest;
import com.example.HealthTracker.model.DTO.UserNameRequest;
import com.example.HealthTracker.model.DTO.UserTrackerRecords;
import com.example.HealthTracker.model.DailyTracker;
import com.example.HealthTracker.model.Users;
import com.example.HealthTracker.service.JwtService;
import com.example.HealthTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(
        origins = {
                "*"
//                "http://localhost:5173",
//                "http://192.168.31.44:5173"
        }
)
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/search/{username}")
    public ResponseEntity<Boolean> searchUser(@PathVariable String username) {

        boolean exists = service.findUser(username);

        return ResponseEntity.ok(exists);
    }

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
    public ResponseEntity<?> addUser(@RequestBody Users user) {
        try {
            return switch (service.saveUser(user)) {
                case CREATED -> ResponseEntity.ok("User created successfully");
                case CONFLICT -> ResponseEntity.badRequest().body("User already exists");
                default -> ResponseEntity.badRequest().body("User not found")  ;
            };
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
//        return new String();y
    }

    @PostMapping("/addDailyProgress/{id}")
    @PreAuthorize("#id == authentication.name")
    public ResponseEntity<?> addDailyTracker(
            @PathVariable String id,
            @RequestBody DailyTracker dailyTracker) {

        try {

            switch (service.addDailyTracker(id, dailyTracker)) {

                case CREATED:
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body("Record has been added successfully");

                case CONFLICT:
                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body("Record cannot be added");

                default:
                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Unknown error occurred");
            }

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PutMapping("editDailyProgress/{id}")
    @PreAuthorize("#id == authentication.name")
    public ResponseEntity<?> editDailyTracker(@PathVariable
                                        String id,
                                        @RequestBody DailyTracker dailyTracker) {
        try {

            switch (service.addDailyTracker(id, dailyTracker)) {

                case CREATED:
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body("Record has been changed successfully");

                case CONFLICT:
                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body("Record cannot be Edited or does not exists");

                default:
                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Unknown error occurred");
            }

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
//        return service.getUsers().toString();
    }

    @DeleteMapping("/deleteUser/{id}")
    @PreAuthorize("#id == authentication.name")
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
    public ResponseEntity<String> login(@RequestBody LoginRequest login) {

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    login.uname(),
                                    login.password()
                            )
                    );

            if(authentication.isAuthenticated()) {
                return ResponseEntity.ok(jwtService.generateToken(login.uname()));
            }

        } catch (Exception e) {
//            System.out.println("Authentication failed: " + e.getMessage()+ login);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
        }

//        System.out.println("Username and password are incorrect : "+login);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
    }

}
