package com.example.blackfriday.service.EventProduct;

import com.example.blackfriday.annotation.DistributedLock;
import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.domain.EventProduct;
import com.example.blackfriday.exception.event.EventProductNotFoundException;
import com.example.blackfriday.repository.EventProductRepository;
import com.example.blackfriday.service.redis.RedissonLockHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Redisson을 이용한 동시성 처리
 * redissonClient의 getLock 메소드를 통해 처리
 */
@Service
@RequiredArgsConstructor
public class EventProductServiceV3Impl implements EventProductService {

    private final DefaultEventProductService defaultEventProductService;
    private final EventProductRepository eventProductRepository;

    @DistributedLock(key = "redisson_lock:")
    @Override
    public void processEventProduct(OrderDto.EventOrderRequest req, Long eventProductId, LocalDateTime currentTime) throws InterruptedException {
        defaultEventProductService.decreaseQuantity(eventProductId, currentTime);
    }
}
