package com.example.blackfriday.repository.redis;

import com.example.blackfriday.exception.event.EventAlreadyParticipationException;
import com.example.blackfriday.exception.event.EventProductQuantityException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
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

    @Transactional
    public void execute(String k, String v, int q) {
        Long cnt = this.sCard(k);
        if (q < cnt) {
            throw new EventProductQuantityException("이벤트 상품 재고가 소진되었습니다.");
        }
        if (this.isMember(k, v)) {
            throw new EventAlreadyParticipationException("해당 이벤트를 이미 참여하였습니다.");
        }
        this.sAdd(k, v);
    }
}
