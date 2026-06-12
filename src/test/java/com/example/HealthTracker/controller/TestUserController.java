package com.example.HealthTracker.controller;

import com.example.HealthTracker.config.TestSecurityConfig;
import com.example.HealthTracker.model.DTO.LoginRequest;
import com.example.HealthTracker.model.DTO.UserTrackerRecords;
import com.example.HealthTracker.service.JwtService;
import com.example.HealthTracker.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(UserController.class)
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService service;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Test
    void shouldReturnTrueWhenUserExists() throws Exception {

        Mockito.when(service.findUser("john"))
                .thenReturn(true);

        mockMvc.perform(get("/search/john"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExist() throws Exception {

        Mockito.when(service.findUser("john"))
                .thenReturn(false);

        mockMvc.perform(get("/search/john"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {

        UserTrackerRecords user =
                Mockito.mock(UserTrackerRecords.class);

        Mockito.when(service.getUsers())
                .thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {

        LoginRequest request =
                new LoginRequest("john", "password");

        Authentication authentication =
                Mockito.mock(Authentication.class);

        Mockito.when(authentication.isAuthenticated())
                .thenReturn(true);

        Mockito.when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        Mockito.when(jwtService.generateToken("john"))
                .thenReturn("jwt-token");

        mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));
    }

    @Test
    void shouldReturnUnauthorizedWhenLoginFails() throws Exception {

        LoginRequest request =
                new LoginRequest("john", "wrongpassword");

        Mockito.when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }
}