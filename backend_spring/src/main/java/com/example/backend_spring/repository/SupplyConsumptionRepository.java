package com.example.backend_spring.repository;

import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.StandardRoomsInfo;
import com.example.backend_spring.entity.Supply;
import com.example.backend_spring.entity.SupplyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplyConsumptionRepository extends JpaRepository<SupplyConsumption, Long> {

    SupplyConsumption findByStandardRoomsInfoAndSupplyAndAppUser(StandardRoomsInfo standardRoomsInfo, Supply supply, AppUser appUser);
    List<SupplyConsumption> findByAppUser(AppUser appUser);
}
