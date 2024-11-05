package com.example.backend_spring;

import com.example.backend_spring.entity.TestEntity;
import com.example.backend_spring.repository.TestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class BackendSpringApplicationTests {

	@Autowired
	private TestRepository testRepository;

	@Test
	void contextLoads() {

		TestEntity entity = new TestEntity();
		entity.setSubject("hello");
		entity.setContent("world");
		entity.setCreateDate(LocalDateTime.now());
		testRepository.save(entity);
	}

}
