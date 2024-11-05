package com.example.backend_spring.service;

import com.example.backend_spring.entity.TestEntity;
import com.example.backend_spring.repository.TestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public List<TestEntity> getList() {
        return testRepository.findAll();
    }
}
