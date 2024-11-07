package com.example.backend_spring.service;

import com.example.backend_spring.entity.StandardRoomsInfo;
import com.example.backend_spring.repository.StandardRoomsInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class StandardRoomsInfoService {

    private StandardRoomsInfoRepository standardRoomsInfoRepository;

    public StandardRoomsInfoService(StandardRoomsInfoRepository standardRoomsInfoRepository) {
        this.standardRoomsInfoRepository = standardRoomsInfoRepository;
    }

    public StandardRoomsInfo create(StandardRoomsInfo standardRoomsInfo) {
        standardRoomsInfoRepository.save(standardRoomsInfo);
        return standardRoomsInfo;
    }

}
