package com.example.backend_spring.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveData(String key, Object value, Duration duration) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, duration);
    }

    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean existData(String key) {
        return redisTemplate.hasKey(key);
    }

    public void removeData(String key) {
        redisTemplate.delete(key);
    }
}
