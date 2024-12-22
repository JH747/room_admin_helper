package com.example.backend_spring.controller;

import com.example.backend_spring.dto.PlatformsAuthInfoDTO;
import com.example.backend_spring.dto.PlatformsRoomsInfoDTO;
import com.example.backend_spring.dto.StandardRoomsInfoDTO;
import com.example.backend_spring.entity.PlatformsAuthInfo;
import com.example.backend_spring.entity.PlatformsRoomsInfo;
import com.example.backend_spring.entity.StandardRoomsInfo;
import com.example.backend_spring.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @PostMapping("/platformsAuthInfo")
    public ResponseEntity<String> setPlatformAuthInfo(@RequestBody PlatformsAuthInfoDTO platformsAuthInfoDTO){
        // create, update, delete
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        settingsService.setPlatformsAuthInfo(platformsAuthInfoDTO, username);
        return ResponseEntity.status(HttpStatus.OK).body("PlatformsAuthInfo Setting succeeded");
    }
    @GetMapping("/platformsAuthInfo")
    public ResponseEntity<PlatformsAuthInfo> getPlatformAuthInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        PlatformsAuthInfo entity = settingsService.getPlatformsAuthInfo(username);
        return ResponseEntity.status(HttpStatus.OK).body(entity);
    }


    @PostMapping("/standardRoomsInfo")
    public ResponseEntity<String> setStandardRoomsInfo(@RequestBody StandardRoomsInfoDTO standardRoomsInfoDTO,
                                                       @RequestParam(defaultValue = "false") boolean delete){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(delete) settingsService.deleteStandardRoomsInfo(standardRoomsInfoDTO, username);
        else settingsService.createStandardRoomsInfo(standardRoomsInfoDTO, username);
        return ResponseEntity.status(HttpStatus.OK).body("StandardRoomsInfo Setting succeeded");
    }
    @GetMapping("/standardRoomsInfo")
    public ResponseEntity<List<StandardRoomsInfo>> getStandardRoomsInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<StandardRoomsInfo> entities = settingsService.getStandardRoomsInfo(username);
        return ResponseEntity.status(HttpStatus.OK).body(entities);
    }


    @PostMapping("/platformsRoomsInfo")
    public ResponseEntity<String> setPlatformsRoomsInfo(@RequestBody PlatformsRoomsInfoDTO platformsRoomsInfoDTO,
                                                        @RequestParam(defaultValue = "false") boolean delete){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(delete) settingsService.deletePlatformsRoomsInfo(platformsRoomsInfoDTO, username);
        else settingsService.createPlatformsRoomsInfo(platformsRoomsInfoDTO, username);
        return ResponseEntity.status(HttpStatus.OK).body("PlatformsRoomsInfo Setting succeeded");
    }
    @GetMapping("/platformsRoomsInfo")
    public ResponseEntity<List<PlatformsRoomsInfoDTO>> getPlatformsRoomsInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<PlatformsRoomsInfoDTO> data = settingsService.getPlatformsRoomsInfo(username);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }


    // controller 내부 전역 예외처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleSpecificControllerExceptions(Exception e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error");
    }
}
