package com.example.backend_spring.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AppUserDTO {
    private String id;
    private String pass;
    @Email
    private String email;
    private String code;
}
