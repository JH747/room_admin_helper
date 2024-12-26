package com.example.backend_spring.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SupplyConsumptionDTO {

    @NotBlank
    private String standardRoomName;

    @NotBlank
    private String supplyName;

    @NotBlank
    @PositiveOrZero
    private Integer consumption;
}
