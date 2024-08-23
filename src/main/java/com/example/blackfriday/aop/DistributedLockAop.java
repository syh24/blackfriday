package com.example.blackfriday.aop;

import com.example.blackfriday.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {

    private final RedissonClient redissonClient;

    @Around(value = "@annotation(com.example.blackfriday.annotation.DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        String lockName = distributedLock.key();

        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals("eventProductId")) {
                lockName = lockName.concat(args[i].toString());
            }
        }

        RLock lock = redissonClient.getLock(lockName);

        try {
            if (!lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit())) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("락 획득 실패");
            }

            return joinPoint.proceed();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new InterruptedException();
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                log.error("락 반납 오류", e);
            }
        }
    }
}
