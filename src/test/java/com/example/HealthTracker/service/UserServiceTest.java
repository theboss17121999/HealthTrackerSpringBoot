package com.example.HealthTracker.service;

import com.example.HealthTracker.model.DTO.UserTrackerRecords;
import com.example.HealthTracker.model.DailyTracker;
import com.example.HealthTracker.model.Users;
import com.example.HealthTracker.repo.DailyTrackerRepo;
import com.example.HealthTracker.repo.UserRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepo userRepo;

    @Mock
    DailyTrackerRepo dailyTrackerRepo;

    @InjectMocks
    UserService userService;

    @BeforeAll
    public static void printA() {
        System.out.println("Print All Users");
    }

    @BeforeEach
    public void setUp() {
        System.out.println("Print All Users again");
    }

    @Test
    void getUsers() {
        Users user = new Users();
        when(userRepo.findAll()).thenReturn(List.of(user));

        List<UserTrackerRecords> userTrackerRecords = userService.getUsers();

        assertEquals(userTrackerRecords.size(), 1);
    }

    @Test
    void testGetUsersSucces() {
        Users user = new Users();
        user.setFirstName("Silent");
        user.setLastName("Geeks");
        when(userRepo.findById("Silent")).thenReturn(Optional.of(user));
        Optional<UserTrackerRecords> userTrackerRecords = userService.getUsers("Silent");

        assertEquals("Silent",userTrackerRecords.get().firstName());
    }

    @Test
    void testGetUsersFailed() {
        Users user = new Users();
        user.setFirstName("Silent");
        user.setLastName("Geeks");
        when(userRepo.findById("Silent")).thenReturn(Optional.empty());
        Optional<UserTrackerRecords> userTrackerRecords = userService.getUsers("Silent");

        assertEquals(Optional.empty(),userTrackerRecords);
    }

    @Test
    void saveUserSucess() throws Exception {
        System.out.println("saveUserSucess");

        Users user = new Users();
        user.setUname("admin");
        user.setPassword("admin123");
        user.setEmail("admin@example.com");

        Mockito.when(userRepo.save(Mockito.any(Users.class))).thenReturn(user);

        HttpStatus s= userService.saveUser(user,"Admin");

        //test
        Assertions.assertEquals(HttpStatus.CREATED,s);
        verify(userRepo).save(Mockito.any(Users.class));
    }
    @Test
    void saveUserFail() throws Exception {
        System.out.println("saveUserFailed");

        Users user = new Users();
        user.setUname("admin");
        user.setPassword("admin123");
        user.setEmail("admin@example.com");

        Mockito.when(userRepo.findById("admin"))
                .thenReturn(Optional.of(user));

        HttpStatus s= userService.saveUser(user,"Admin");

        //test
        Assertions.assertEquals(HttpStatus.CONFLICT, s);
        Assertions.assertEquals(HttpStatus.CONFLICT, s);

        verify(userRepo, Mockito.never())
                .save(Mockito.any(Users.class));
    }

    @Test
    void addDailyTrackerSucess() throws Exception {

        Users user = new Users();
        user.setUname("admin");
        user.setPassword("admin123");
        user.setDailyTrackers(new ArrayList<>());

        DailyTracker dailyTracker = new DailyTracker();


        when(userRepo.findById("admin")).thenReturn(Optional.of(user));

        HttpStatus httpStatus = userService.addDailyTracker("admin",dailyTracker);

        assertEquals(HttpStatus.CREATED, httpStatus);

        verify(userRepo).save(Mockito.any(Users.class));

    }

    @Test
    void addDailyTrackerFail() throws Exception {
        Users user = new Users();
        user.setUname("admin");
        user.setPassword("admin123");
        user.setDailyTrackers(new ArrayList<>());

        DailyTracker dailyTracker = new DailyTracker();

        when(userRepo.findById("admin")).thenReturn(Optional.empty());

        HttpStatus httpStatus = userService.addDailyTracker("admin",dailyTracker);

        assertEquals(HttpStatus.CONFLICT, httpStatus);
    }

    @Test
    void editDailyTracker() {
    }

    @Test
    void getUserTrackerRecords() {
    }

    @Test
    void deleteUserSucess() {
        Users user = new Users();
        user.setUname("admin");

        when(userRepo.findById("admin"))
                .thenReturn(Optional.of(user));

        when(dailyTrackerRepo.findByUname("admin"))
                .thenReturn(Collections.emptyList());

        doNothing().when(dailyTrackerRepo).deleteAll(anyList());
        doNothing().when(userRepo).delete(any(Users.class));

        userService.deleteUser("admin");

        verify(dailyTrackerRepo, times(1)).deleteAll(anyList());
        verify(userRepo, times(1)).delete(user);
    }

    @Test
    void testPrivateFieldP() throws Exception {
        Field field = UserService.class.getDeclaredField("p");
        field.setAccessible(true);

        int value = (int) field.get(userService);

        assertEquals(1, value);
    }

    @Test
    void testPrivateMethod_getUserTrackeRecords() throws Exception {
        Users user = new Users();
        user.setUname("admin");
        user.setEmail("admin@test.com");
        user.setFirstName("Admin");
        user.setLastName("User");

        Method method = UserService.class.getDeclaredMethod(
                "getUserTrackerRecords",
                Users.class
        );
        method.setAccessible(true);

        UserTrackerRecords result =
                (UserTrackerRecords) method.invoke(userService, user);

        System.out.println(result);

        assertEquals("admin", result.uname());
        assertEquals("admin@test.com", result.email());
        assertEquals("Admin", result.firstName());
        assertEquals("User", result.lastName());
    }

    @Test
    void deleteUserFailRuntimeException() {
        Users user = new Users();
        user.setUname("admin");
        user.setPassword("admin123");

        when(userRepo.findById("admin")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.deleteUser("admin")
        );

        assertEquals("User not found", exception.getMessage());

    }

    @Test
    void trackerRecordsShouldThrowExceptionForInvalidUser() {
        System.out.println("trackerRecordsShouldThrowExceptionForInvalidUser");

        Users user = new Users();

        when(userRepo.findById("admin"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.trackerRecords("admin")
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void findUserSuccess() {
        Users user = new Users();
        user.setUname("admin");
        user.setPassword("admin123");


        when(userRepo.findById("admin")).thenReturn(Optional.of(user));

        assertEquals(true, userService.findUser("admin"));

    }

    @Test
    void findUserFail() {
        Users user = new Users();
        user.setUname("admin");
        user.setPassword("admin123");


        when(userRepo.findById("admin")).thenReturn(Optional.empty());

        assertEquals(false, userService.findUser("admin"));
    }

    @AfterAll
    public static void Destroy() {
        System.out.println("Destroy");
    }

    @AfterEach
    public void cleanUp() {
        System.out.println("CleanUp");
    }
}