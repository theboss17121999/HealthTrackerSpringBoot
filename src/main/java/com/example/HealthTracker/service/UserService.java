package com.example.HealthTracker.service;

import com.example.HealthTracker.CustomAnnotations.AgeAnnotation;
import com.example.HealthTracker.CustomAnnotations.UserServiceAnnotation;
import com.example.HealthTracker.CustomException.UseNamerNotAllowed;
import com.example.HealthTracker.model.DTO.DailyTrackerData;
import com.example.HealthTracker.model.DTO.Nutrients;
import com.example.HealthTracker.model.DTO.UserTrackerRecords;
import com.example.HealthTracker.model.DailyTracker;
import com.example.HealthTracker.model.NutrientsInFlow;
import com.example.HealthTracker.model.Users;
import com.example.HealthTracker.repo.DailyTrackerRepo;
import com.example.HealthTracker.repo.NutrientsInFlowRepo;
import com.example.HealthTracker.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@UserServiceAnnotation
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DailyTrackerRepo dailyTrackerRepo;

    @Autowired
    private NutrientsInFlowRepo nutrientsInFlowRepo;

    private int p=1;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    @Async
    @Cacheable(value = "User" , key = "#pageable")
    public CompletableFuture<Page<UserTrackerRecords>> findAllUsers(Pageable pageable) {
        try{
//            System.out.println("Async thread: " + Thread.currentThread().getName());
            Random random = new Random();

//            try {
//                Thread.sleep(10000); // Simulate long-running work
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }

//            System.out.println("Running on thread: " + Thread.currentThread().getName());

            return CompletableFuture.completedFuture(userRepo.findAll(pageable)
                    .map(this::getUserTrackerRecords)
            );
        }
        catch (Exception e){
            System.out.println("this is the GlobalExceptionHandler");
            e.printStackTrace();
            throw e;
        }

    }


    @Async
    @Cacheable(value = "User")
    public CompletableFuture<List<UserTrackerRecords>> getUsers() {
//        Random random = new Random();
//        if(random.nextInt(2)==1)
//            throw new RuntimeException("Simulated GlobalExceptionHandler");

//        try {
//            Thread.sleep(5000); // Simulate long-running work
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }

        System.out.println("Running on thread: " + Thread.currentThread().getName());

        List<Users> users = userRepo.findAll();

        List<UserTrackerRecords> result = users.stream()
                .map(this::getUserTrackerRecords)
                .toList();

        return CompletableFuture.completedFuture(result);
    }

    @AgeAnnotation
    @Cacheable(value = "User", key="#id")
    public Optional<UserTrackerRecords> getUsers(String id) {

        Optional<Users> user = userRepo.findById(id);

        if (user.isPresent()) {
            return Optional.of(getUserTrackerRecords(user.get()));
        }

        return Optional.empty();
    }

    @AgeAnnotation(minAgeRequired = 21)
    @CachePut(value = "User", key = "#user.getUname()")
    public HttpStatus saveUser(Users user, String role) throws Exception{
        if(user.getUname().equals("admin")){
            throw new UseNamerNotAllowed("Username admin not allowed");
        }
        boolean usernameAvailable =
                userRepo.findById(user.getUname()).isEmpty();

        boolean emailAvailable =
                !userRepo.existsByEmail(user.getEmail());

        user.setRole(role);
        if(user.getDailyTrackers() == null){
            user.setDailyTrackers(new ArrayList<>());
        }

        if(usernameAvailable && emailAvailable) {
            for (DailyTracker tracker : user.getDailyTrackers()) {
                tracker.setUser(user);
            }
            user.setPassword(encoder.encode(user.getPassword()));
            userRepo.save(user);
            return HttpStatus.CREATED;
        }
        else  {
            return HttpStatus.CONFLICT;
        }
    }

    @CachePut(value= "Tracker", key = "#id + '_' + #dailyTracker.getDate()")
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

    @Cacheable(value = "Tracker", key = "#id + '_' + #reqDailyTracker.getDate()")
    public HttpStatus editDailyTracker(String id, DailyTracker reqDailyTracker) {
//        System.out.println(reqDailyTracker);
        Date date = new Date(reqDailyTracker.getDate().getTime());

        Optional<Users> userAvailable = userRepo.findById(id);

        Optional<DailyTracker> userDailyTracker = dailyTrackerRepo.findByUnameAndDate(id,date);

//        System.out.println("Tracker :"+userDailyTracker.isPresent());
//        System.out.println("User :"+userAvailable.isPresent());
        if(userAvailable.isPresent() && userDailyTracker.isPresent()) {
//            System.out.println(" here");
            NutrientsInFlow nutrients = userDailyTracker.get().getNutrients();
            reqDailyTracker.getNutrients().setId(nutrients.getId());

            nutrientsInFlowRepo.save(reqDailyTracker.getNutrients());
//            System.out.println("Nutrients :"+nutrientsInFlowRepo.findAll());

            return HttpStatus.ACCEPTED;
        }
        return HttpStatus.NOT_FOUND;
    }

    private UserTrackerRecords getUserTrackerRecords(Users user) {
        return new UserTrackerRecords(
                user.getUname(),
//                "wrong",
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getHeight(),
                user.getWeight()
        );
    }

    //this method will delete user and all the date corresponding to it
    @Transactional
    @CacheEvict(value = "User", key="#id")
    public void deleteUser(String id) {

        Users user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<DailyTracker> trackers = dailyTrackerRepo.findByUname(id);

        dailyTrackerRepo.deleteAll(trackers);

        userRepo.delete(user);
    }

    @Cacheable(value = "user", key = "id")
    public List<DailyTrackerData> trackerRecords(String id) {
        Optional<Users> user = userRepo.findById(id);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        List<DailyTracker> dailyTrackers = dailyTrackerRepo.findByUname(id);
        return dailyTrackers.stream()
                .map(tracker -> new DailyTrackerData(
                        tracker.getDate(),
                        new Nutrients(
                                tracker.getNutrients().getFat(),
                                tracker.getNutrients().getProtein(),
                                tracker.getNutrients().getCarbohydrate(),
                                tracker.getNutrients().getFiber()
                        )
                ))
                .toList();
    }

    @Cacheable(value = "user", key = "#username")
    public boolean findUser(String username) {
        if(username.equals("admin")){
            throw new UseNamerNotAllowed("Username admin not allowed");
        }
        if(username == null || username.isEmpty() || username.length() <= 4 || Character.isDigit(username.charAt(0))){
            return false;
        }
        Optional<Users> user = userRepo.findById(username);
        if (user.isEmpty()) {
            return false;
        }
        else {
            return true;
        }
    }

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class);

    @Transactional
    @CacheEvict(value = "Tracker", key = "#id + '_' + #date" )
    public void deleteTrackerbyID(String id, Date date) {

        List<DailyTracker> tracker1 = dailyTrackerRepo.findByUname(id);
//        System.out.println(date.toString());
//        for (DailyTracker dailyTracker : tracker1) {
//            System.out.println(dailyTracker.getDate());
//        }

        Optional<DailyTracker> tracker = dailyTrackerRepo.findByUserByDate(id,date);
//        System.out.println(tracker.get());
        if (tracker.isEmpty()) {
            logger.warn("Tracker not found: {}", id);
            throw new RuntimeException("Tracker not found");
        }

        // delete the found entity to avoid EmptyResultDataAccessException and
        // ensure JPA cascades/relationships are handled correctly
        dailyTrackerRepo.delete(tracker.get());
    }

    @Cacheable(value = "Tracker" , key = "#id")
    public Page<DailyTrackerData> trackerRecords(String id, Pageable pageable) {
        Optional<Users> user = userRepo.findById(id);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Page<DailyTracker> dailyTrackers = dailyTrackerRepo.findByUname(id,pageable);
        return dailyTrackers
                .map(tracker -> new DailyTrackerData(
                        tracker.getDate(),
                        new Nutrients(
                                tracker.getNutrients().getFat(),
                                tracker.getNutrients().getProtein(),
                                tracker.getNutrients().getCarbohydrate(),
                                tracker.getNutrients().getFiber()
                        )
                ));
    }
}
