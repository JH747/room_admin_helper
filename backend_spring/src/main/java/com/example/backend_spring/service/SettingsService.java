package com.example.backend_spring.service;

import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.PlatformsAuthInfo;
import com.example.backend_spring.entity.PlatformsRoomsInfo;
import com.example.backend_spring.entity.StandardRoomsInfo;
import com.example.backend_spring.repository.AppUserRepository;
import com.example.backend_spring.repository.PlatformsAuthInfoRepository;
import com.example.backend_spring.repository.PlatformsRoomsInfoRepository;
import com.example.backend_spring.repository.StandardRoomsInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsService {

    private final AppUserRepository appUserRepository;
    private final PlatformsAuthInfoRepository platformsAuthInfoRepository;
    private final StandardRoomsInfoRepository standardRoomsInfoRepository;
    private final PlatformsRoomsInfoRepository platformsRoomsInfoRepository;

    public SettingsService(AppUserRepository appUserRepository,
                           PlatformsAuthInfoRepository platformsAuthInfoRepository,
                           StandardRoomsInfoRepository standardRoomsInfoRepository,
                           PlatformsRoomsInfoRepository platformsRoomsInfoRepository){
        this.appUserRepository = appUserRepository;
        this.platformsAuthInfoRepository = platformsAuthInfoRepository;
        this.standardRoomsInfoRepository = standardRoomsInfoRepository;
        this.platformsRoomsInfoRepository = platformsRoomsInfoRepository;
    }

    public PlatformsAuthInfo createPlatformsAuthInfo(PlatformsAuthInfo platformsAuthInfo, String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        platformsAuthInfo.setAppUser(appUser);
        platformsAuthInfoRepository.save(platformsAuthInfo);
        return platformsAuthInfo;
    }

    public PlatformsAuthInfo getPlatformsAuthInfo(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        return platformsAuthInfoRepository.findByAppUser(appUser);
    }

    public StandardRoomsInfo createStandardRoomsInfo(StandardRoomsInfo standardRoomsInfo, String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        standardRoomsInfo.setAppUser(appUser);
        standardRoomsInfoRepository.save(standardRoomsInfo);
        return standardRoomsInfo;
    }

    public List<StandardRoomsInfo> getStandardRoomsInfo(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        return standardRoomsInfoRepository.findByAppUser(appUser);
    }

    public PlatformsRoomsInfo createPlatformsRoomsInfo(PlatformsRoomsInfo platformsRoomsInfo, String username, String standard_room_name) {
        AppUser appUser = appUserRepository.findByUsername(username);
        StandardRoomsInfo standardRoomsInfo = standardRoomsInfoRepository.findByRoomName(standard_room_name);
        platformsRoomsInfo.setAppUser(appUser);
        platformsRoomsInfo.setStandardRoomsInfo(standardRoomsInfo);
        platformsRoomsInfoRepository.save(platformsRoomsInfo);
        return platformsRoomsInfo;
    }

    public List<PlatformsRoomsInfo> getPlatformsRoomsInfo(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        return platformsRoomsInfoRepository.findByAppUser(appUser);
    }

}
