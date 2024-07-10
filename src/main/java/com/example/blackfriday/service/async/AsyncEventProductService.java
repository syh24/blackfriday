package com.example.blackfriday.service.async;

import com.example.blackfriday.config.component.RabbitMqProducer;
import com.example.blackfriday.controller.dto.EventProductMessageDto;
import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.domain.redis.EventProductRedis;
import com.example.blackfriday.domain.redis.EventRedis;
import com.example.blackfriday.exception.event.EventProductQuantityException;
import com.example.blackfriday.repository.RedisRepository;
import com.example.blackfriday.service.cache.EventCacheService;
import com.example.blackfriday.service.cache.EventProductCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.blackfriday.utils.RedisKeyUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncEventProductService {

    private final RabbitMqProducer producer;
    private final RedisRepository redisRepository;
    private final EventProductCacheService eventProductCacheService;
    private final EventCacheService eventCacheService;

    //Rabbit Queue에 이벤트 상품 요청 적재
    public void processEventProduct(OrderDto.EventOrderRequest req, Long eventProductId, LocalDateTime currentTime) {
        checkValidEventDateAndTime(req.getEventId(), currentTime);
        EventProductRedis eventProduct = eventProductCacheService.getEventProduct(eventProductId);
        checkValidEventProductRequest(eventProduct, req.getMemberId());
        producer.producer(new EventProductMessageDto(req.getMemberId(), eventProductId));
    }

    private void checkValidEventProductRequest(EventProductRedis eventProduct, Long memberId) {
        int q = eventProduct.eventQuantity();
        String key = getEventProductRequestKey(eventProduct.eventProductId());
        Long count = redisRepository.increment(key);
        if (q < count) {
            throw new EventProductQuantityException("이벤트 상품 재고가 소진되었습니다.");
        }
    }

    private void checkValidEventDateAndTime(Long eventId, LocalDateTime currentTime) {
        EventRedis event = eventCacheService.getEvent(eventId);
        event.checkValidEvent(currentTime);
    }
}
