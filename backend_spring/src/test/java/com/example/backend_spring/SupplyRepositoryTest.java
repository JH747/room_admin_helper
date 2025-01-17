package com.example.backend_spring;

import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.Supply;
import com.example.backend_spring.repository.AppUserRepository;
import com.example.backend_spring.repository.SupplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // 실제 DB 사용
public class SupplyRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private SupplyRepository supplyRepository;


    @BeforeEach
    public void before(){
        AppUser user = new AppUser();
        user.setUsername("test_user");
        user.setPassword("test_password");
        user.setEmail("test_user@example.com");
        appUserRepository.save(user);
    }

    @Test
    public void testFindByNameAndAppUser(){
        // given
        AppUser testUser = appUserRepository.findByUsername("test_user");
        Supply expectedSupply = new Supply();
        expectedSupply.setName("test_supply");
        expectedSupply.setAppUser(testUser);
        supplyRepository.save(expectedSupply);
        // when
        Supply givenSupply = supplyRepository.findByNameAndAppUser("test_supply", testUser);
        // then
        assertEquals(expectedSupply, givenSupply);
    }

    @Test
    public void testFindByAppUser(){
        // given
        AppUser testUser = appUserRepository.findByUsername("test_user");
        List<Supply> expectedSupplies = new ArrayList<>();
        Supply supply = new Supply();
        supply.setName("test_supply1");
        supply.setAppUser(testUser);
        expectedSupplies.add(supply);
        supply = new Supply();
        supply.setName("test_supply2");
        supply.setAppUser(testUser);
        expectedSupplies.add(supply);
        supplyRepository.saveAll(expectedSupplies);
        // when
        List<Supply> givenSupplies = supplyRepository.findByAppUser(testUser);
        // then
        assertEquals(expectedSupplies, givenSupplies);
    }
}
