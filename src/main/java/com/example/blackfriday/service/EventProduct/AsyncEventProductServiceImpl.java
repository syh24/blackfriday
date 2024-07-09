package com.example.blackfriday.service.EventProduct;

import com.example.blackfriday.config.component.RabbitMqProducer;
import com.example.blackfriday.controller.dto.EventProductMessageDto;
import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.domain.EventProduct;
import com.example.blackfriday.domain.redis.EventProductRedis;
import com.example.blackfriday.domain.redis.EventRedis;
import com.example.blackfriday.exception.event.EventAlreadyParticipationException;
import com.example.blackfriday.exception.event.EventProductNotFoundException;
import com.example.blackfriday.exception.event.EventProductQuantityException;
import com.example.blackfriday.repository.EventProductRepository;
import com.example.blackfriday.repository.RedisRepository;
import com.example.blackfriday.service.cache.EventCacheService;
import com.example.blackfriday.service.cache.EventProductCacheService;
import com.example.blackfriday.utils.RedisKeyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.blackfriday.utils.RedisKeyUtils.*;

@Service
@RequiredArgsConstructor
public class AsyncEventProductServiceImpl implements EventProductService {

    private final RabbitMqProducer producer;
    private final RedisRepository redisRepository;
    private final EventProductCacheService eventProductCacheService;
    private final EventCacheService eventCacheService;

    //Rabbit Queue에 이벤트 상품 요청 적재
    @Override
    public void processEventProduct(OrderDto.EventOrderRequest req, Long eventProductId, LocalDateTime currentTime) {
        checkValidEventDateAndTime(req.getEventId(), currentTime);
        EventProductRedis eventProduct = eventProductCacheService.getEventProduct(eventProductId);
        checkValidEventProductRequest(eventProduct, req.getMemberId());
        redisRepository.setAdd(getEventProductRequestKey(eventProductId), req.getMemberId().toString());
        producer.producer(new EventProductMessageDto(req.getMemberId(), eventProductId));
    }

    private void checkValidEventProductRequest(EventProductRedis eventProduct, Long memberId) {
        int q = eventProduct.eventQuantity();
        String key = getEventProductRequestKey(eventProduct.eventProductId());
        if (q <= redisRepository.setSize(key)) {
            throw new EventProductQuantityException("이벤트 상품 재고가 소진되었습니다.");
        }
        if (redisRepository.isMember(key, memberId.toString())) {
            throw new EventAlreadyParticipationException("이미 해당 이벤트 상품을 구입했습니다.");
        }
    }

    private void checkValidEventDateAndTime(Long eventId, LocalDateTime currentTime) {
        EventRedis event = eventCacheService.getEvent(eventId);
        event.checkValidEvent(currentTime);
    }
}
