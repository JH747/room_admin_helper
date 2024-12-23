package com.example.backend_spring.repository;

import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.Supply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplyRepository extends JpaRepository<Supply, Integer> {

    Supply findByNameAndAppUser(String name, AppUser appUser);
    List<Supply> findByAppUser(AppUser appUser);
}
