package com.example.backend_spring.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemoDTO {

    private Integer id;
    @NotBlank(message = "title should not be blank")
    private String roomTitle;
    private String content;

}
