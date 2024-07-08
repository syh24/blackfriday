package com.example.blackfriday.service.cache;

import com.example.blackfriday.domain.EventProduct;
import com.example.blackfriday.exception.event.EventProductNotFoundException;
import com.example.blackfriday.repository.EventProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventProductCacheService {

    private final EventProductRepository eventProductRepository;

    @Cacheable(key = "#eventProductId", value = "eventProduct")
    public EventProduct getEventProduct(Long eventProductId) {
        return eventProductRepository.findById(eventProductId)
                .orElseThrow(() -> new EventProductNotFoundException("해당 이벤트 상품을 찾을 수 없습니다."));
    }
}
