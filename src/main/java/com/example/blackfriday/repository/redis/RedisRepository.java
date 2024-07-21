package com.example.blackfriday.repository.redis;

import com.example.blackfriday.exception.event.EventAlreadyParticipationException;
import com.example.blackfriday.exception.event.EventProductQuantityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public Long sAdd(String k, String v) {
        return redisTemplate.opsForSet().add(k, v);
    }

    public Long sCard(String k) {
        return redisTemplate.opsForSet().size(k);
    }

    public Boolean isMember(String k, String v) {
        return redisTemplate.opsForSet().isMember(k, v);
    }

    public void execute(String k, String v, int q) {
        // Lua 스크립트 작성
        final String script =
                """
                    if redis.call('SCARD', KEYS[1]) >= tonumber(ARGV[1]) then
                    return -1
                    elseif redis.call('SISMEMBER', KEYS[1], ARGV[2]) == 1 then
                    return -2
                    else
                    redis.call('SADD', KEYS[1], ARGV[2])
                    return 1
                    end
                """;

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(script);
        redisScript.setResultType(Long.class);

        Long result = redisTemplate.execute(redisScript, Collections.singletonList(k), String.valueOf(q), v);

        if (result == -1) {
            throw new EventProductQuantityException("이벤트 상품 재고가 소진되었습니다.");
        } else if (result == -2) {
            throw new EventAlreadyParticipationException("해당 이벤트를 이미 참여하였습니다.");
        }
    }
}
