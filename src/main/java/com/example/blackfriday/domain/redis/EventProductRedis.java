package com.example.blackfriday.domain.redis;

import com.example.blackfriday.domain.EventProduct;
import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("eventProduct")
public record EventProductRedis (
    @Id
    Long eventProductId,
    int eventQuantity

) {
    public EventProductRedis(EventProduct eventProduct) {
        this (
                eventProduct.getId(),
                eventProduct.getEventQuantity()
        );
    }
}
