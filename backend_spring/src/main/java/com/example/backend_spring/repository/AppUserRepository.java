package com.example.backend_spring.repository;

import com.example.backend_spring.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    AppUser findByUsername(String username);

}
