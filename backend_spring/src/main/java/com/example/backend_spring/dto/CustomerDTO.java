package com.example.backend_spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerDTO {

    @NotBlank
    private String name;
    private String note;

}
