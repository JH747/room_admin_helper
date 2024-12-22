package com.example.backend_spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StandardRoomsInfoDTO {

    @NotBlank
    private String roomName;

    @NotBlank @Positive
    private Integer roomQuantity;

    @NotBlank @PositiveOrZero
    private Integer displayOrder;

}
