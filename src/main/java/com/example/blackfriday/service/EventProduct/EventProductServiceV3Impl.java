package com.example.blackfriday.service.EventProduct;

import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.domain.EventProduct;
import com.example.blackfriday.exception.event.EventProductNotFoundException;
import com.example.blackfriday.repository.EventProductRepository;
import com.example.blackfriday.service.DefaultEventProductService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Redisson을 이용한 동시성 처리
 * redissonClient의 getLock 메소드를 통해 처리
 */
@Service
@RequiredArgsConstructor
public class EventProductServiceV3Impl implements EventProductService{

    private final RedissonClient redissonClient;
    private final DefaultEventProductService defaultEventProductService;

    @Override
    public void processEventProduct(OrderDto.OrderRequest req, Long productId, LocalDateTime currentTime) throws InterruptedException {
        final String worker = Thread.currentThread().getName();
        RLock lock = redissonClient.getLock("redisson_lock p" + productId + "e" + req.getEventId());

        try {
            //획득시도 시간, 락 점유 시간
            if (!lock.tryLock(5, 3, TimeUnit.SECONDS)) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("락 획득 실패");
            }

            defaultEventProductService.decreaseQuantity(productId, req, currentTime);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                System.out.println("락 반납 오류");
            }
        }
    }
}
