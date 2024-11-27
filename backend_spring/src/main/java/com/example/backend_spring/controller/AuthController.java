package com.example.backend_spring.controller;

import com.example.backend_spring.JWTUtil;

import com.example.backend_spring.service.AppUserService;
import com.example.backend_spring.service.EmailService;
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
    private final EmailService emailService;

    public AuthController(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserSecurityService userDetailsService, AppUserService appUserService, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.appUserService = appUserService;
        this.emailService = emailService;
    }

    @PostMapping("/sendcode")
    public ResponseEntity<String> sendCode(@RequestBody HashMap<String, Object> userData) {
        String email = (String) userData.get("email");

        emailService.sendCode(email);
        return ResponseEntity.ok(String.format("Verification mail successfully sent to %s", email));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody HashMap<String, Object> userData){
        String username = (String) userData.get("id");
        String password = (String) userData.get("pass");
        String email = (String) userData.get("email");
        String code = (String) userData.get("code");

        if(!emailService.verifyCode(email, code)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong verification code");

        try{
            appUserService.create(username, password, email);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicating username");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("User %s created successfully", username));
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody HashMap<String, Object> userData){
        String username = (String) userData.get("id");
        String password = (String) userData.get("pass");

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
