package com.example.HealthTracker.service;

import com.example.HealthTracker.model.DTO.UserTrackerRecords;
import com.example.HealthTracker.model.DailyTracker;
import com.example.HealthTracker.model.NutrientsInFlow;
import com.example.HealthTracker.model.Users;
import com.example.HealthTracker.repo.DailyTrackerRepo;
import com.example.HealthTracker.repo.NutrientsInFlowRepo;
import com.example.HealthTracker.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DailyTrackerRepo dailyTrackerRepo;

    @Autowired
    private NutrientsInFlowRepo nutrientsInFlowRepo;


    public List<Users> getUsers() {
        return userRepo.findAll();
    }

    public Optional<UserTrackerRecords> getUsers(String id) {

        Optional<Users> user = userRepo.findById(id);

        if (user.isPresent()) {
            return Optional.of(getUserTrackerRecords(user.get()));
        }

        return Optional.empty();
    }

    public HttpStatus saveUser(Users user) throws Exception{
        boolean usernameAvailable =
                userRepo.findById(user.getUname()).isEmpty();

        boolean emailAvailable =
                !userRepo.existsByEmail(user.getEmail());
        if(user.getDailyTrackers() == null){
            user.setDailyTrackers(new ArrayList<>());
        }

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

    public HttpStatus editDailyTracker(String id, Date date, DailyTracker reqDailyTracker) {
        date = new Date(date.getTime() + (5 * 60 * 60 * 1000) + (30 * 60 * 1000));

        Optional<Users> userAvailable = userRepo.findById(id);

        Optional<DailyTracker> userDailyTracker = dailyTrackerRepo.findByUnameAndDate(id,date);
        if(userAvailable.isPresent() && userDailyTracker.isPresent()) {
            //after finding and storing nutrients for DailyTracker for perticular date
            NutrientsInFlow nutrients = userDailyTracker.get().getNutrients();
//            System.out.println(" - - - - -  -Nutritions (find) :"+nutrients);

            //changing Id for reqDailytracker and saving it, so that new record is not created in the table
            reqDailyTracker.getNutrients().setId(nutrients.getId());
//            System.out.println(" - - - - -  -Nutritions (to be set) :"+reqDailyTracker.getNutrients());

            nutrientsInFlowRepo.save(reqDailyTracker.getNutrients());

            return HttpStatus.ACCEPTED;
        }
        return HttpStatus.NOT_FOUND;
    }

    public UserTrackerRecords getUserTrackerRecords(Users user) {
        return new UserTrackerRecords(
                user.getUname(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getHeight(),
                user.getWeight(),
                user.getDailyTrackers()
        );
    }
}
