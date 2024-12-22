package com.example.backend_spring.service;

import com.example.backend_spring.dto.PlatformsAuthInfoDTO;
import com.example.backend_spring.dto.PlatformsRoomsInfoDTO;
import com.example.backend_spring.dto.StandardRoomsInfoDTO;
import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.PlatformsAuthInfo;
import com.example.backend_spring.entity.PlatformsRoomsInfo;
import com.example.backend_spring.entity.StandardRoomsInfo;
import com.example.backend_spring.repository.AppUserRepository;
import com.example.backend_spring.repository.PlatformsAuthInfoRepository;
import com.example.backend_spring.repository.PlatformsRoomsInfoRepository;
import com.example.backend_spring.repository.StandardRoomsInfoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public PlatformsAuthInfo setPlatformsAuthInfo(PlatformsAuthInfoDTO platformsAuthInfoDTO, String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        PlatformsAuthInfo authInfo = platformsAuthInfoRepository.findByAppUser(appUser);
        if(authInfo != null){
            platformsAuthInfoRepository.delete(authInfo);
        }
        authInfo = new PlatformsAuthInfo();
        // supports yapen, yogei by now
        authInfo.setAppUser(appUser);
        authInfo.setYapenId(platformsAuthInfoDTO.getYapenId());
        authInfo.setYapenPass(platformsAuthInfoDTO.getYapenPass());
        authInfo.setYogeiId(platformsAuthInfoDTO.getYogeiId());
        authInfo.setYogeiPass(platformsAuthInfoDTO.getYogeiPass());
        platformsAuthInfoRepository.save(authInfo);
        return authInfo;
    }

    public PlatformsAuthInfo getPlatformsAuthInfo(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        return platformsAuthInfoRepository.findByAppUser(appUser);
    }

    public void createStandardRoomsInfo(StandardRoomsInfoDTO standardRoomsInfoDTO, String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        StandardRoomsInfo sri = new StandardRoomsInfo();
        sri.setAppUser(appUser);
        sri.setRoomName(standardRoomsInfoDTO.getRoomName());
        sri.setRoomQuantity(standardRoomsInfoDTO.getRoomQuantity());
        sri.setDisplayOrder(standardRoomsInfoDTO.getDisplayOrder());
        standardRoomsInfoRepository.save(sri);
    }
    public void deleteStandardRoomsInfo(StandardRoomsInfoDTO standardRoomsInfoDTO, String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        StandardRoomsInfo sri = standardRoomsInfoRepository.findByRoomNameAndAppUser(standardRoomsInfoDTO.getRoomName(), appUser);
        if(sri != null) standardRoomsInfoRepository.delete(sri);
    }
    public List<StandardRoomsInfo> getStandardRoomsInfo(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        return standardRoomsInfoRepository.findByAppUser(appUser);
    }


    public void createPlatformsRoomsInfo(PlatformsRoomsInfoDTO platformsRoomsInfoDTO, String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        StandardRoomsInfo sri = standardRoomsInfoRepository.findByRoomNameAndAppUser(platformsRoomsInfoDTO.getStadardRoomName(), appUser);
        PlatformsRoomsInfo pri = new PlatformsRoomsInfo();
        pri.setAppUser(appUser);
        pri.setStandardRoomsInfo(sri);
        pri.setYapenRoomName(platformsRoomsInfoDTO.getYapenRoomName());
        pri.setYogeiRoomName(platformsRoomsInfoDTO.getYogeiRoomName());
        pri.setDisplayOrder(platformsRoomsInfoDTO.getDisplayOrder());
        platformsRoomsInfoRepository.save(pri);
    }
    public void deletePlatformsRoomsInfo(PlatformsRoomsInfoDTO platformsRoomsInfoDTO, String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        StandardRoomsInfo sri = standardRoomsInfoRepository.findByRoomNameAndAppUser(platformsRoomsInfoDTO.getStadardRoomName(), appUser);
        PlatformsRoomsInfo pri = platformsRoomsInfoRepository.findByStandardRoomsInfoAndAppUser(sri, appUser);
        if(pri != null) platformsRoomsInfoRepository.delete(pri);
    }
    public List<PlatformsRoomsInfoDTO> getPlatformsRoomsInfo(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        List<PlatformsRoomsInfo> pris = platformsRoomsInfoRepository.findByAppUser(appUser);
        List<PlatformsRoomsInfoDTO> priDTOs = new ArrayList<>();
        for(PlatformsRoomsInfo pri : pris){
            PlatformsRoomsInfoDTO priDTO = new PlatformsRoomsInfoDTO();
            priDTO.setStadardRoomName(pri.getStandardRoomsInfo().getRoomName());
            priDTO.setYapenRoomName(pri.getYapenRoomName());
            priDTO.setYogeiRoomName(pri.getYogeiRoomName());
            priDTO.setDisplayOrder(pri.getDisplayOrder());
        }
        return priDTOs;
    }

}
