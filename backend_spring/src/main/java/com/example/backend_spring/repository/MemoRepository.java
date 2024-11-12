package com.example.backend_spring.repository;

import com.example.backend_spring.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemoRepository extends JpaRepository<Memo, Integer> {

}
