package com.example.backend_spring.service;

import com.example.backend_spring.dto.SupplyConsumptionDTO;
import com.example.backend_spring.dto.SupplyDTO;
import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.StandardRoomsInfo;
import com.example.backend_spring.entity.Supply;
import com.example.backend_spring.entity.SupplyConsumption;
import com.example.backend_spring.repository.AppUserRepository;
import com.example.backend_spring.repository.StandardRoomsInfoRepository;
import com.example.backend_spring.repository.SupplyConsumptionRepository;
import com.example.backend_spring.repository.SupplyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplyService {

    private final AppUserRepository appUserRepository;
    private final StandardRoomsInfoRepository standardRoomsInfoRepository;
    private final SupplyRepository supplyRepository;
    private final SupplyConsumptionRepository supplyConsumptionRepository;

    public SupplyService(AppUserRepository appUserRepository, StandardRoomsInfoRepository standardRoomsInfoRepository,
                         SupplyRepository supplyRepository, SupplyConsumptionRepository supplyConsumptionRepository) {
        this.appUserRepository = appUserRepository;
        this.standardRoomsInfoRepository = standardRoomsInfoRepository;
        this.supplyRepository = supplyRepository;
        this.supplyConsumptionRepository = supplyConsumptionRepository;
    }

    public void createSupply(SupplyDTO supplyDTO, String username){
        AppUser appUser = appUserRepository.findByUsername(username);
        Supply supply = new Supply();
        supply.setAppUser(appUser);
        supply.setName(supplyDTO.getName());
        supply.setDesiredQuantity(supplyDTO.getDesiredQuantity());
        supply.setCurrentQuantity(supplyDTO.getCurrentQuantity());
        supply.setThresholdQuantity(supplyDTO.getThresholdQuantity());
        supplyRepository.save(supply);
    }
    public void deleteSupply(SupplyDTO supplyDTO, String username){
        AppUser appUser = appUserRepository.findByUsername(username);
        Supply supply = supplyRepository.findByNameAndAppUser(supplyDTO.getName(), appUser);
        if(supply != null){
            supplyRepository.delete(supply);
        }
    }
    public List<SupplyDTO> getSupply(String username){
        AppUser appUser = appUserRepository.findByUsername(username);
        List<Supply> supplies = supplyRepository.findByAppUser(appUser);
        List<SupplyDTO> supplyDTOs = new ArrayList<>();
        for(Supply supply : supplies){
            SupplyDTO supplyDTO = new SupplyDTO();
            supplyDTO.setName(supply.getName());
            supplyDTO.setDesiredQuantity(supply.getDesiredQuantity());
            supplyDTO.setCurrentQuantity(supply.getCurrentQuantity());
            supplyDTO.setThresholdQuantity(supply.getThresholdQuantity());
            supplyDTOs.add(supplyDTO);
        }
        return supplyDTOs;
    }

    public void createSupplyConsumption(SupplyConsumptionDTO supplyConsumptionDTO, String username){
        AppUser appUser = appUserRepository.findByUsername(username);
        StandardRoomsInfo sri = standardRoomsInfoRepository.findByRoomNameAndAppUser(supplyConsumptionDTO.getStandardRoomsName(), appUser);
        Supply supply = supplyRepository.findByNameAndAppUser(supplyConsumptionDTO.getSupplyName(), appUser);
        SupplyConsumption supplyConsumption = new SupplyConsumption();
        supplyConsumption.setAppUser(appUser);
        supplyConsumption.setStandardRoomsInfo(sri);
        supplyConsumption.setSupply(supply);
        supplyConsumptionRepository.save(supplyConsumption);
    }
    public void deleteSupplyConsumption(SupplyConsumptionDTO supplyConsumptionDTO, String username){
        AppUser appUser = appUserRepository.findByUsername(username);
        StandardRoomsInfo sri = standardRoomsInfoRepository.findByRoomNameAndAppUser(supplyConsumptionDTO.getStandardRoomsName(), appUser);
        Supply supply = supplyRepository.findByNameAndAppUser(supplyConsumptionDTO.getSupplyName(), appUser);
        SupplyConsumption supplyConsumption = supplyConsumptionRepository.findByStandardRoomsInfoAndSupplyAndAppUser(sri, supply, appUser);
        if(supplyConsumption != null){
            supplyConsumptionRepository.delete(supplyConsumption);
        }
    }
    public List<SupplyConsumptionDTO> getSupplyConsumption(String username){
        AppUser appUser = appUserRepository.findByUsername(username);
        List<SupplyConsumption> supplyConsumptions = supplyConsumptionRepository.findByAppUser(appUser);
        List<SupplyConsumptionDTO> supplyConsumptionDTOs = new ArrayList<>();
        for(SupplyConsumption supplyConsumption : supplyConsumptions){
            SupplyConsumptionDTO supplyConsumptionDTO = new SupplyConsumptionDTO();
            supplyConsumptionDTO.setStandardRoomsName(supplyConsumption.getStandardRoomsInfo().getRoomName());
            supplyConsumptionDTO.setSupplyName(supplyConsumption.getSupply().getName());
            supplyConsumptionDTOs.add(supplyConsumptionDTO);
        }
        return supplyConsumptionDTOs;
    }
}
