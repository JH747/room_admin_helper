package com.example.backend_spring.controller;

import com.example.backend_spring.entity.PlatformsAuthInfo;
import com.example.backend_spring.entity.PlatformsRoomsInfo;
import com.example.backend_spring.entity.StandardRoomsInfo;
import com.example.backend_spring.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    private final AppUserService appUserService;
    private final SettingsService settingsService;

    public SettingsController(AppUserService appUserService,
                              SettingsService settingsService) {
        this.appUserService = appUserService;
        this.settingsService = settingsService;
    }

    @PostMapping("/platformsAuthInfo")
    public ResponseEntity<String> setPlatformAuth(@RequestBody PlatformsAuthInfo authInfo){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        settingsService.createPlatformsAuthInfo(authInfo, username);

        return ResponseEntity.ok("PlatformsAuthInfo Setting succeeded");
    }

    @PostMapping("/standardRoomsInfo")
    public ResponseEntity<String> setStandardRoomsInfo(@RequestBody StandardRoomsInfo standardRoomsInfo){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        settingsService.createStandardRoomsInfo(standardRoomsInfo, username);

        return ResponseEntity.ok("StandardRoomsInfo Setting succeeded");
    }

    @PostMapping("/platformsRoomsInfo")
    public ResponseEntity<String> setPlatformsRoomsInfo(@RequestBody HashMap<String, Object> data){
        String standard_room_name = (String) data.get("standardRoomName");
        String yapen_room_name = (String) data.get("yapenRoomName");
        String yogei_room_name = (String) data.get("yogeiRoomName");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        PlatformsRoomsInfo platformsRoomsInfo = new PlatformsRoomsInfo();
        platformsRoomsInfo.setYapenRoomName(yapen_room_name);
        platformsRoomsInfo.setYogeiRoomName(yogei_room_name);
        settingsService.createPlatformsRoomsInfo(platformsRoomsInfo, username, standard_room_name);

        return ResponseEntity.ok("PlatformsRoomsInfo Setting succeeded");
    }
}
