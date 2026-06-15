package com.example.HealthTracker.controller;

import com.example.HealthTracker.model.DTO.LoginRequest;
import com.example.HealthTracker.model.Users;
import com.example.HealthTracker.service.JwtService;
import com.example.HealthTracker.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService service;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @org.junit.jupiter.api.Test
    void searchUser_ReturnsTrue() throws Exception {

        when(service.findUser("john")).thenReturn(true);

        mockMvc.perform(get("/search/john"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @org.junit.jupiter.api.Test
    void register_ReturnsSuccess_WhenUserCreated() throws Exception {

        Users user = new Users();
        user.setUname("alice");
        user.setPassword("password123");
        user.setEmail("alice@example.com");

        when(service.saveUser(any(Users.class), eq("ROLE_USER")))
                .thenReturn(HttpStatus.CREATED);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("User created successfully"));
    }

    @org.junit.jupiter.api.Test
    void register_ReturnsBadRequest_WhenUserAlreadyExists() throws Exception {

        Users user = new Users();
        user.setUname("alice");
        user.setPassword("password123");
        user.setEmail("alice@example.com");

        when(service.saveUser(any(Users.class), eq("ROLE_USER")))
                .thenReturn(HttpStatus.CONFLICT);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User already exists"));
    }

    @org.junit.jupiter.api.Test
    void login_ReturnsJwtToken_WhenAuthenticated() throws Exception {

        LoginRequest login = new LoginRequest("bob", "secret");

        Authentication authentication = Mockito.mock(Authentication.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(jwtService.generateToken("bob"))
                .thenReturn("mock-jwt-token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(content().string("mock-jwt-token"));
    }

    @org.junit.jupiter.api.Test
    void login_ReturnsUnauthorized_WhenAuthenticationFails() throws Exception {

        LoginRequest login = new LoginRequest("bob", "wrong-password");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Bad credentials"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));

    }
}
