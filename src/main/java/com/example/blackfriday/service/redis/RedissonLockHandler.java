package com.example.blackfriday.service.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedissonLockHandler {

    private final RedissonClient client;

    public void execute(String name, long waitTime, long leaseTime, Runnable runnable) {
        RLock lock = client.getLock(name);

        try {
            if (!lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("락 획득 실패");
            }
            runnable.run();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                log.error("락 반납 오류", e);
            }
        }
    }
}
