package com.example.backend_spring.controller;

import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.StandardRoomsInfo;
import com.example.backend_spring.repository.AppUserRepository;
import com.example.backend_spring.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    private AppUserRepository appUserRepository;



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/administratoronly")
    public ResponseEntity<String> getAdministratorOnly() {
        return ResponseEntity.ok("you have administrator authority");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/authenticatedonly")
    public ResponseEntity<String> getRes() {
        return ResponseEntity.ok("Hello World");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/authenticatedonly/1")
    public ResponseEntity<String> test() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.isAuthenticated());
        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.getName());

        return ResponseEntity.ok("Hello, World!");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/authenticatedonly/2")
    public ResponseEntity<String> getData() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        AppUser appUser = appUserRepository.findByUsername(username);

        System.out.println(appUser.getStandardRoomsInfos());

        for(StandardRoomsInfo sri : appUser.getStandardRoomsInfos()) {
            System.out.println(sri.getId() + "\t" + sri.getDisplayOrder() + "\t" + sri.getRoomQuantity() + "\t" + sri.getRoomName());
        }

        return ResponseEntity.ok("Hello, World!");
    }

}
