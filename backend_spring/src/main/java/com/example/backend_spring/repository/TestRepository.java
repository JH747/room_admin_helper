package com.example.backend_spring.repository;

import com.example.backend_spring.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TestRepository extends JpaRepository<TestEntity, Integer> {

}
