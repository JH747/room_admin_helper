package com.example.backend_spring.controller;

import com.example.backend_spring.JWTUtil;

import com.example.backend_spring.service.AppUserService;
import com.example.backend_spring.service.UserSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserSecurityService userDetailsService;
    private final AppUserService appUserService;

    public AuthController(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserSecurityService userDetailsService, AppUserService appUserService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.appUserService = appUserService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody HashMap<String, Object> userData){
        String username = (String) userData.get("username");
        String password = (String) userData.get("password");
        String email = (String) userData.get("email");

        appUserService.create(username, password, email);

        String response = String.format("User %s created successfully", username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody HashMap<String, Object> userData){
        String username = (String) userData.get("username");
        String password = (String) userData.get("password");

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(token);

        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }
}
