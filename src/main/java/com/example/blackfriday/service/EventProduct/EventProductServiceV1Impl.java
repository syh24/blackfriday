package com.example.blackfriday.service.EventProduct;

import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.domain.EventProduct;
import com.example.blackfriday.exception.event.EventProductNotFoundException;
import com.example.blackfriday.repository.EventProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
public class EventProductServiceV1Impl implements EventProductService {

    private final EventProductRepository eventProductRepository;

    @Override
    public void processEventProduct(OrderDto.EventOrderRequest req, Long eventProductId,  LocalDateTime currentTime) {
        EventProduct eventProduct = eventProductRepository.findById(eventProductId)
                        .orElseThrow(() -> new EventProductNotFoundException("해당 이벤트 상품을 찾을 수 없습니다."));

        eventProduct.decreaseEventQuantity();
    }

}
