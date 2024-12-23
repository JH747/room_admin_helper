package com.example.backend_spring.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SupplyDTO {

    @NotBlank
    private String name;

    @NotBlank
    @PositiveOrZero
    private Integer desiredQuantity; // 사용자 설정

    @NotBlank
    @PositiveOrZero
    private Integer thresholdQuantity; // 사용자 설정

    @NotBlank
    @PositiveOrZero
    private Integer currentQuantity; // 사용자 업데이트
}
