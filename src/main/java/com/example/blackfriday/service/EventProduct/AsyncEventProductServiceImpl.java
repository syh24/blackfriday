package com.example.blackfriday.service.EventProduct;

import com.example.blackfriday.config.component.RabbitMqProducer;
import com.example.blackfriday.controller.dto.EventProductMessageDto;
import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.domain.EventProduct;
import com.example.blackfriday.exception.event.EventProductNotFoundException;
import com.example.blackfriday.repository.EventProductRepository;
import com.example.blackfriday.service.cache.EventProductCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AsyncEventProductServiceImpl implements EventProductService {

    private final RabbitMqProducer producer;
    private final EventProductCacheService eventProductCacheService;

    //Rabbit Queue에 이벤트 상품 요청 적재
    @Override
    public void processEventProduct(OrderDto.EventOrderRequest req, LocalDateTime currentTime) {
        EventProduct eventProduct = eventProductCacheService.getEventProduct(req.getEventProductId());
        producer.producer(new EventProductMessageDto(req.getMemberId(), eventProduct.getId()));
    }
}
