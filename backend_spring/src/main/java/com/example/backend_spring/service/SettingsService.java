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

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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

    public void setPlatformsAuthInfo(PlatformsAuthInfoDTO platformsAuthInfoDTO, String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        PlatformsAuthInfo authInfo = platformsAuthInfoRepository.findByAppUser(appUser);
        if(authInfo != null){
            platformsAuthInfoRepository.delete(authInfo);
        }
        authInfo = new PlatformsAuthInfo();
        // supports yapen, yogei only by now
        authInfo.setAppUser(appUser);
        authInfo.setYapenId(platformsAuthInfoDTO.getYapenId());
        authInfo.setYapenPass(platformsAuthInfoDTO.getYapenPass());
        authInfo.setYogeiId(platformsAuthInfoDTO.getYogeiId());
        authInfo.setYogeiPass(platformsAuthInfoDTO.getYogeiPass());
        platformsAuthInfoRepository.save(authInfo);
    }
    public PlatformsAuthInfoDTO getPlatformsAuthInfo(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        PlatformsAuthInfo pai = platformsAuthInfoRepository.findByAppUser(appUser);
        PlatformsAuthInfoDTO paiDTO = new PlatformsAuthInfoDTO();
        // supports yapen, yogei only by now
        paiDTO.setYapenId(pai.getYapenId());
        paiDTO.setYogeiId(pai.getYogeiId());
        return paiDTO;
    }

    public void createStandardRoomsInfo(StandardRoomsInfoDTO standardRoomsInfoDTO, String username) throws Exception {
        AppUser appUser = appUserRepository.findByUsername(username);
        StandardRoomsInfo sri = new StandardRoomsInfo();
        sri.setAppUser(appUser);
        sri.setRoomName(standardRoomsInfoDTO.getRoomName());
        sri.setRoomQuantity(standardRoomsInfoDTO.getRoomQuantity());
        sri.setDisplayOrder(standardRoomsInfoDTO.getDisplayOrder());
        try {
            standardRoomsInfoRepository.save(sri);
        } catch (Exception e) {
            throw e; // - refactor
        }
    }
    public void deleteStandardRoomsInfo(StandardRoomsInfoDTO standardRoomsInfoDTO, String username) throws Exception {
        AppUser appUser = appUserRepository.findByUsername(username);
        StandardRoomsInfo sri = standardRoomsInfoRepository.findByRoomNameAndAppUser(standardRoomsInfoDTO.getRoomName(), appUser);
        if(sri != null) standardRoomsInfoRepository.delete(sri);
        else throw new Exception("Corresponding standard room does not exist");
    }
    public List<StandardRoomsInfoDTO> getStandardRoomsInfo(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        List<StandardRoomsInfo> sris = standardRoomsInfoRepository.findByAppUser(appUser);
        List<StandardRoomsInfoDTO> sriDTOs = new ArrayList<>();
        for(StandardRoomsInfo sri : sris){
            StandardRoomsInfoDTO sriDTO = new StandardRoomsInfoDTO();
            sriDTO.setRoomName(sri.getRoomName());
            sriDTO.setRoomQuantity(sri.getRoomQuantity());
            sriDTO.setDisplayOrder(sri.getDisplayOrder());
            sriDTOs.add(sriDTO);
        }
        return sriDTOs;
    }


    public void createPlatformsRoomsInfo(PlatformsRoomsInfoDTO platformsRoomsInfoDTO, String username) throws Exception{
        AppUser appUser = appUserRepository.findByUsername(username);
        StandardRoomsInfo sri = standardRoomsInfoRepository.findByRoomNameAndAppUser(platformsRoomsInfoDTO.getStandardRoomName(), appUser);
        if(sri == null){
            throw new Exception("Corresponding standard room does not exist");
        }
        PlatformsRoomsInfo pri = new PlatformsRoomsInfo();
        pri.setAppUser(appUser);
        pri.setStandardRoomsInfo(sri);
        pri.setYapenRoomName(platformsRoomsInfoDTO.getYapenRoomName());
        pri.setYogeiRoomName(platformsRoomsInfoDTO.getYogeiRoomName());
        pri.setDisplayOrder(platformsRoomsInfoDTO.getDisplayOrder());
        try{
            platformsRoomsInfoRepository.save(pri);
        } catch (Exception e) {
            throw e; // - refactor
        }
    }
    public void deletePlatformsRoomsInfo(PlatformsRoomsInfoDTO platformsRoomsInfoDTO, String username) throws Exception{
        AppUser appUser = appUserRepository.findByUsername(username);
        StandardRoomsInfo sri = standardRoomsInfoRepository.findByRoomNameAndAppUser(platformsRoomsInfoDTO.getStandardRoomName(), appUser);
        if(sri == null){
            throw new Exception("Corresponding standard room does not exist");
        }
        PlatformsRoomsInfo pri = platformsRoomsInfoRepository.findByStandardRoomsInfoAndAppUser(sri, appUser);
        if(pri != null) platformsRoomsInfoRepository.delete(pri);
        else throw new Exception("Corresponding platform room does not exist");
    }
    public List<PlatformsRoomsInfoDTO> getPlatformsRoomsInfo(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        List<PlatformsRoomsInfo> pris = platformsRoomsInfoRepository.findByAppUser(appUser);
        List<PlatformsRoomsInfoDTO> priDTOs = new ArrayList<>();
        for(PlatformsRoomsInfo pri : pris){
            PlatformsRoomsInfoDTO priDTO = new PlatformsRoomsInfoDTO();
            priDTO.setStandardRoomName(pri.getStandardRoomsInfo().getRoomName());
            priDTO.setYapenRoomName(pri.getYapenRoomName());
            priDTO.setYogeiRoomName(pri.getYogeiRoomName());
            priDTO.setDisplayOrder(pri.getDisplayOrder());
            priDTOs.add(priDTO);
        }
        return priDTOs;
    }

}
