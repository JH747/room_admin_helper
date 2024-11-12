package com.example.backend_spring.service;

import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser create(String username, String password, String email){
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(passwordEncoder.encode(password));
        appUser.setEmail(email);
        appUserRepository.save(appUser);
        return appUser;
    }

    public AppUser findByUsername(String username){
        return appUserRepository.findByUsername(username);
    }
}
