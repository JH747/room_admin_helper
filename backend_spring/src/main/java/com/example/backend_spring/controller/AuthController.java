package com.example.backend_spring.controller;

import com.example.backend_spring.JWTUtil;

import com.example.backend_spring.dto.AppUserDTO;
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
    public ResponseEntity<String> sendCode(@RequestBody AppUserDTO appUserDTO) {
        // email format verified at front
        emailService.sendCode(appUserDTO.getEmail());
        return ResponseEntity.ok(String.format("Verification mail successfully sent to %s", appUserDTO.getEmail()));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AppUserDTO appUserDTO){
        if(!emailService.verifyCode(appUserDTO.getEmail(), appUserDTO.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong verification code");
        }
        try{
            appUserService.create(appUserDTO.getId(), appUserDTO.getPass(), appUserDTO.getEmail());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicating username");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("User %s created successfully", appUserDTO.getId()));
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody AppUserDTO appUserDTO){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUserDTO.getId(), appUserDTO.getPass()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(appUserDTO.getId());
            String token = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(token);
        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }


    // controller 내부 전역 예외처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleSpecificControllerExceptions(Exception e){
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }
}
