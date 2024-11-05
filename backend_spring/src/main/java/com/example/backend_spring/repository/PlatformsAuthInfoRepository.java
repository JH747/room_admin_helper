package com.example.backend_spring.repository;

import com.example.backend_spring.entity.PlatformsAuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformsAuthInfoRepository extends JpaRepository<PlatformsAuthInfo, Integer> {
}
