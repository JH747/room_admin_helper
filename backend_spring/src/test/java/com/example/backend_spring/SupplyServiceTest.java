package com.example.backend_spring;

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
import com.example.backend_spring.service.SupplyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
public class SupplyServiceTest {

    @MockBean
    private AppUserRepository appUserRepository;
    @MockBean
    private StandardRoomsInfoRepository standardRoomsInfoRepository;
    @MockBean
    private SupplyRepository supplyRepository;
    @MockBean
    private SupplyConsumptionRepository supplyConsumptionRepository;

    @Autowired
    private SupplyService supplyService;

    @BeforeEach
    public void before(){
        AppUser user = new AppUser();
        user.setUsername("test_user");
        user.setPassword("test_password");
        user.setEmail("test_user@example.com");
        when(appUserRepository.findByUsername("test_user")).thenReturn(user);

        List<Supply> supplies = new ArrayList<>();
        Supply supply1 = new Supply();
        supply1.setName("test_supply");
        supply1.setDesiredQuantity(10);
        supply1.setCurrentQuantity(8);
        supply1.setThresholdQuantity(5);
        supplies.add(supply1);
        Supply supply2 = new Supply();
        supply2.setName("test_supply2");
        supply2.setDesiredQuantity(20);
        supply2.setCurrentQuantity(16);
        supply2.setThresholdQuantity(10);
        supplies.add(supply2);
        when(supplyRepository.findByAppUser(user)).thenReturn(supplies);

        StandardRoomsInfo sri = new StandardRoomsInfo();
        sri.setRoomName("test_room");

        List<SupplyConsumption> supplyConsumptions = new ArrayList<>();
        SupplyConsumption supplyConsumption1 = new SupplyConsumption();
        supplyConsumption1.setSupply(supply1);
        supplyConsumption1.setStandardRoomsInfo(sri);
        supplyConsumptions.add(supplyConsumption1);

        SupplyConsumption supplyConsumption2 = new SupplyConsumption();
        supplyConsumption2.setSupply(supply2);
        supplyConsumption2.setStandardRoomsInfo(sri);
        supplyConsumptions.add(supplyConsumption2);

        when(supplyConsumptionRepository.findByAppUser(user)).thenReturn(supplyConsumptions);
    }

    @Test
    public void testGetSupply(){
        // given
        List<SupplyDTO> expectedSupplyDTOS = new ArrayList<>();
        SupplyDTO supplyDTO = new SupplyDTO();
        supplyDTO.setName("test_supply");
        supplyDTO.setDesiredQuantity(10);
        supplyDTO.setCurrentQuantity(8);
        supplyDTO.setThresholdQuantity(5);
        expectedSupplyDTOS.add(supplyDTO);
        supplyDTO = new SupplyDTO();
        supplyDTO.setName("test_supply2");
        supplyDTO.setDesiredQuantity(20);
        supplyDTO.setCurrentQuantity(16);
        supplyDTO.setThresholdQuantity(10);
        expectedSupplyDTOS.add(supplyDTO);
        // when
        List<SupplyDTO> givenSupplyDTOs = supplyService.getSupply("test_user");
        // then
        for(int i = 0; i < 2; i++){
            assertEquals(expectedSupplyDTOS.get(i).getName(), givenSupplyDTOs.get(i).getName());
        }
    }

    @Test
    public void testGetSupplyConsumption(){
        // given
        List<SupplyConsumptionDTO> expectedSupplyConsumptionDTOS = new ArrayList<>();
        SupplyConsumptionDTO supplyConsumptionDTO = new SupplyConsumptionDTO();
        supplyConsumptionDTO.setStandardRoomName("test_room");
        supplyConsumptionDTO.setSupplyName("test_supply");
        supplyConsumptionDTO.setConsumption(10);
        expectedSupplyConsumptionDTOS.add(supplyConsumptionDTO);
        supplyConsumptionDTO = new SupplyConsumptionDTO();
        supplyConsumptionDTO.setStandardRoomName("test_room");
        supplyConsumptionDTO.setSupplyName("test_supply2");
        supplyConsumptionDTO.setConsumption(20);
        expectedSupplyConsumptionDTOS.add(supplyConsumptionDTO);
        // when
        List<SupplyConsumptionDTO> givenSupplyConsumptionDTO = supplyService.getSupplyConsumption("test_user");
        // then
        for(int i = 0; i < 2; i++){
            assertEquals(expectedSupplyConsumptionDTOS.get(i).getSupplyName(), givenSupplyConsumptionDTO.get(i).getSupplyName());
        }

    }
}
