package com.example.HealthTracker.config;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfiguration
public class TestSecurityConfig {

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CorsConfigurationSource corsConfigurationSource;

    @InjectMocks
    public SecurityConfig  securityConfig;

    @Test
    void authProviderShouldBeCreated() {
        AuthenticationProvider provider = securityConfig.authProvider();

        assertNotNull(provider);
        assertTrue(provider instanceof DaoAuthenticationProvider);
    }
    @Test
    void authProvider() {
    }

    @Test
    void testSecurityFilterChain() {
    }

    @Test
    void corsConfigurationSource() {
    }

    @Test
    void authenticationManager() {
    }
}