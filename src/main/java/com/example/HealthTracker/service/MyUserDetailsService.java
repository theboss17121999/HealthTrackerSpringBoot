package com.example.HealthTracker.service;

import com.example.HealthTracker.model.UserPrinciple;
import com.example.HealthTracker.model.Users;
import com.example.HealthTracker.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUname(username);

        if(user == null){
            throw new UsernameNotFoundException(username);
        }

        return new UserPrinciple(user);
    }
}
