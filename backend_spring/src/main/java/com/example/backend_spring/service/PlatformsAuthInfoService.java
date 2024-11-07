package com.example.backend_spring.service;

import com.example.backend_spring.entity.PlatformsAuthInfo;
import com.example.backend_spring.repository.PlatformsAuthInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class PlatformsAuthInfoService {

    private final PlatformsAuthInfoRepository platformsAuthInfoRepository;

    public PlatformsAuthInfoService(PlatformsAuthInfoRepository platformsAuthInfoRepository) {
        this.platformsAuthInfoRepository = platformsAuthInfoRepository;
    }

    public PlatformsAuthInfo create(PlatformsAuthInfo platformsAuthInfo) {
        platformsAuthInfoRepository.save(platformsAuthInfo);

        return platformsAuthInfo;
    }
}
