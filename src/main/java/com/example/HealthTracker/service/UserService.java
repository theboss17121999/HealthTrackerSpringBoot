package com.example.HealthTracker.service;

import com.example.HealthTracker.model.DailyTracker;
import com.example.HealthTracker.model.Users;
import com.example.HealthTracker.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public List<Users> getUsers() {
        return userRepo.findAll();
    }

    public Optional<Users> getUsers(String id) {
        return userRepo.findById(id);
    }

    public HttpStatus saveUser(Users user) throws Exception{
        boolean usernameAvailable =
                userRepo.findById(user.getUname()).isEmpty();

        boolean emailAvailable =
                !userRepo.existsByEmail(user.getEmail());

        if(usernameAvailable && emailAvailable) {
            for (DailyTracker tracker : user.getDailyTrackers()) {
                tracker.setUser(user);
            }
            userRepo.save(user);
            return HttpStatus.CREATED;
        }
        else  {
            return HttpStatus.CONFLICT;
        }

    }

    public HttpStatus addDailyTracker(String id, DailyTracker dailyTracker) {
        boolean usernameAvailable =
                !userRepo.findById(id).isEmpty();

        try{
            if(usernameAvailable) {
                Users user = userRepo.findById(id).get();
                dailyTracker.setUser(userRepo.findById(id).get());
                List<DailyTracker> dailyTrackers = user.getDailyTrackers();
                dailyTrackers.add(dailyTracker);
                user.setDailyTrackers(dailyTrackers);
                userRepo.save(user);
                return HttpStatus.CREATED;
            }
        }
        catch(Exception e){
            return HttpStatus.CONFLICT;
        }


        return HttpStatus.CONFLICT;
    }
}
