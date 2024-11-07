package com.example.backend_spring.controller;

import com.example.backend_spring.entity.PlatformsAuthInfo;
import com.example.backend_spring.entity.PlatformsRoomsInfo;
import com.example.backend_spring.entity.StandardRoomsInfo;
import com.example.backend_spring.service.AppUserService;
import com.example.backend_spring.service.PlatformsAuthInfoService;
import com.example.backend_spring.service.PlatformsRoomsInfoService;
import com.example.backend_spring.service.StandardRoomsInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    private final AppUserService appUserService;
    private final PlatformsAuthInfoService platformsAuthInfoService;
    private final StandardRoomsInfoService standardRoomsInfoService;
    private final PlatformsRoomsInfoService platformsRoomsInfoService;

    public SettingsController(AppUserService appUserService,
                              PlatformsAuthInfoService platformsAuthInfoService,
                              StandardRoomsInfoService standardRoomsInfoService,
                              PlatformsRoomsInfoService platformsRoomsInfoService) {
        this.appUserService = appUserService;
        this.platformsAuthInfoService = platformsAuthInfoService;
        this.standardRoomsInfoService = standardRoomsInfoService;
        this.platformsRoomsInfoService = platformsRoomsInfoService;
    }

    @PostMapping("/platformsAuthInfo")
    public ResponseEntity<String> setPlatformAuth(@RequestBody PlatformsAuthInfo authInfo){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authInfo.setApp_user(appUserService.findByUsername(authentication.getName()));
        platformsAuthInfoService.create(authInfo);

        return ResponseEntity.ok("PlatformsAuthInfo Setting succeeded");
    }

    @PostMapping("/standardRoomsInfo")
    public ResponseEntity<String> setStandardRoomsInfo(@RequestBody StandardRoomsInfo standardRoomsInfo){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        standardRoomsInfo.setApp_user(appUserService.findByUsername(authentication.getName()));
        standardRoomsInfoService.create(standardRoomsInfo);

        return ResponseEntity.ok("StandardRoomsInfo Setting succeeded");
    }

    @PostMapping("/platformsRoomsInfo")
    public ResponseEntity<String> setPlatformsRoomsInfo(@RequestBody PlatformsRoomsInfo platformsRoomsInfo){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        platformsRoomsInfo.setApp_user(appUserService.findByUsername(authentication.getName()));
        platformsRoomsInfoService.create(platformsRoomsInfo);

        return ResponseEntity.ok("PlatformsRoomsInfo Setting succeeded");
    }
}
