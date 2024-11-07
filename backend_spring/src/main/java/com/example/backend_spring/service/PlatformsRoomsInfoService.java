package com.example.backend_spring.service;

import com.example.backend_spring.entity.PlatformsRoomsInfo;
import com.example.backend_spring.repository.PlatformsRoomsInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class PlatformsRoomsInfoService {

    private final PlatformsRoomsInfoRepository platformsRoomsInfoRepository;

    public PlatformsRoomsInfoService(PlatformsRoomsInfoRepository platformsRoomsInfoRepository) {
        this.platformsRoomsInfoRepository = platformsRoomsInfoRepository;
    }

    public PlatformsRoomsInfo create(PlatformsRoomsInfo platformsRoomsInfo) {
        platformsRoomsInfoRepository.save(platformsRoomsInfo);
        return platformsRoomsInfo;
    }
}
