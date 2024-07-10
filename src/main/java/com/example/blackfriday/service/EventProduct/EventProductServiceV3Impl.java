package com.example.blackfriday.service.EventProduct;

import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.service.redis.RedissonLockHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Redisson을 이용한 동시성 처리
 * redissonClient의 getLock 메소드를 통해 처리
 */
@Service
@RequiredArgsConstructor
public class EventProductServiceV3Impl implements EventProductService {

    private final RedissonLockHandler redissonLock;
    private final DefaultEventProductService defaultEventProductService;

    @Override
    public void processEventProduct(OrderDto.EventOrderRequest req, Long eventProductId, LocalDateTime currentTime) throws InterruptedException {
        String lockName ="redisson_lock" + eventProductId;
        redissonLock.execute(lockName, 5, 3, () ->
                defaultEventProductService.decreaseQuantity(eventProductId, currentTime)
        );
    }
}
