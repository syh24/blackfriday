package com.example.blackfriday.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public Long sAdd(String k, String v) {
        return redisTemplate.opsForSet().add(k, v);
    }

    public Long sCard(String k) {
        return redisTemplate.opsForSet().size(k);
    }

    public Boolean isMember(String k, String v) {
        return redisTemplate.opsForSet().isMember(k, v);
    }
}
