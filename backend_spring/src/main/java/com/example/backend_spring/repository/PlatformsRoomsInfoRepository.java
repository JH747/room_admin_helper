package com.example.backend_spring.repository;

import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.PlatformsRoomsInfo;
import com.example.backend_spring.entity.StandardRoomsInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatformsRoomsInfoRepository extends JpaRepository<PlatformsRoomsInfo, Integer> {

    public PlatformsRoomsInfo findByStandardRoomsInfoAndAppUser(StandardRoomsInfo standardRoomsInfo, AppUser appUser);
    public List<PlatformsRoomsInfo> findByAppUser(AppUser appUser);
}
