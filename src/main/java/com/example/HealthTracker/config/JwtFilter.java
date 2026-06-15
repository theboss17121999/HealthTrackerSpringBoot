package com.example.HealthTracker.config;

import com.example.HealthTracker.service.JwtService;
import com.example.HealthTracker.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extract the Authorization header from the incoming HTTP request
//        System.out.println("request: "+request); // to print request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;

        // 2. Validate that the header exists and follows the 'Bearer <token>' schema
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extract the actual token strings after "Bearer "
            try{
                userName = jwtService.extractUserName(token);
            }
            catch (io.jsonwebtoken.ExpiredJwtException ex){
                System.err.println(ex.getMessage());
            }
        }

        // 3. If a valid username is found and the user is not already authenticated in this context session
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Fetch user details from the database using your custom UserDetailsService
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(userName);

            // Validate if the token signature matches and hasn't expired yet
            if (jwtService.validateToken(token, userDetails)) {

                // Create an authentication token using Spring Security's core class
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Build and attach additional request web information details (IP address, Session ID)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Update the security context holder with our new verified authentication token
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 4. Always forward the request details down to the next filter in the security execution chain
        filterChain.doFilter(request, response);
    }
}