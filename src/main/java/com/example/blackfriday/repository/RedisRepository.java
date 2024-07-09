package com.example.blackfriday.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public Long setAdd(String k, String v) {
        return redisTemplate.opsForSet().add(k, v);
    }

    public Long setSize(String k) {
        return redisTemplate.opsForSet().size(k);
    }

    public Boolean isMember(String k, String v) {
        return redisTemplate.opsForSet().isMember(k, v);
    }
}
