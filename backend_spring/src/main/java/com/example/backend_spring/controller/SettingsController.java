package com.example.backend_spring.controller;

import com.example.backend_spring.dto.*;
import com.example.backend_spring.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/settings")
public class SettingsController {

    private final SettingsService settingsService;
    private final SupplyService supplyService;

    public SettingsController(SettingsService settingsService, SupplyService supplyService) {
        this.settingsService = settingsService;
        this.supplyService = supplyService;
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
    public ResponseEntity<PlatformsAuthInfoDTO> getPlatformAuthInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        PlatformsAuthInfoDTO data = settingsService.getPlatformsAuthInfo(username);
        return ResponseEntity.status(HttpStatus.OK).body(data);
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
    public ResponseEntity<List<StandardRoomsInfoDTO>> getStandardRoomsInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<StandardRoomsInfoDTO> data = settingsService.getStandardRoomsInfo(username);
        return ResponseEntity.status(HttpStatus.OK).body(data);
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


    @PostMapping("/supply")
    public ResponseEntity<String> setSupply(@RequestBody SupplyDTO supplyDTO,
                                            @RequestParam(defaultValue = "false") boolean delete){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(delete) supplyService.deleteSupply(supplyDTO, username);
        else supplyService.createSupply(supplyDTO, username);
        return ResponseEntity.status(HttpStatus.OK).body("Supply Setting succeeded");
    }
    @GetMapping("/supply")
    public ResponseEntity<List<SupplyDTO>> getSupply(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<SupplyDTO> data = supplyService.getSupply(username);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }


    @PostMapping("supplyConsumption")
    public ResponseEntity<String> setSupplyConsumption(@RequestBody SupplyConsumptionDTO supplyConsumptionDTO,
                                                       @RequestParam(defaultValue = "false") boolean delete){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(delete) supplyService.deleteSupplyConsumption(supplyConsumptionDTO, username);
        else supplyService.createSupplyConsumption(supplyConsumptionDTO, username);
        return ResponseEntity.status(HttpStatus.OK).body("SupplyConsumption Setting succeeded");
    }
    @GetMapping("supplyConsumption")
    public ResponseEntity<List<SupplyConsumptionDTO>> getSupplyConsumption(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<SupplyConsumptionDTO> data = supplyService.getSupplyConsumption(username);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }


    // controller 내부 전역 예외처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleSpecificControllerExceptions(Exception e){
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }
}
