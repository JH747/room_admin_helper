package com.example.backend_spring.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlatformsRoomsInfoDTO {

    @NotBlank
    private String stadardRoomName;
    @NotBlank
    private String yapenRoomName;
    @NotBlank
    private String yogeiRoomName;

    private String naverRoomName;

    private String bnbRoomName;

    @NotBlank @PositiveOrZero
    private Integer displayOrder;
}
