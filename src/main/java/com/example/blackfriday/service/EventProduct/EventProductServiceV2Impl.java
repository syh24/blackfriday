package com.example.blackfriday.service.EventProduct;

import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.domain.EventProduct;
import com.example.blackfriday.exception.event.EventProductNotFoundException;
import com.example.blackfriday.repository.EventProductRepository;
import com.example.blackfriday.repository.LettuceLockRepository;
import com.example.blackfriday.service.DefaultEventProductService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
//@Service
public class EventProductServiceV2Impl implements EventProductService {

    private final LettuceLockRepository lettuceLockRepository;
    private final EventProductRepository eventProductRepository;
    private final DefaultEventProductService defaultEventProductService;

    @Override
    public void processEventProduct(OrderDto.OrderRequest req, Long productId, LocalDateTime currentTime) throws InterruptedException {
        EventProduct eventProduct = eventProductRepository.findEventProductByEventAndProduct(req.getEventId(), productId)
                .orElseThrow(() -> new EventProductNotFoundException("해당 이벤트 상품을 찾을 수 없습니다."));

        Long key = eventProduct.getId();

        //스핀락을 걸어줌
        while (!lettuceLockRepository.lock(key)) {
            //redis의 부화를 줄여주기 위한 sleep
            Thread.sleep(100);
        }

        try {
            //트랜잭션을 안에서 걸어줌
            defaultEventProductService.decreaseQuantity(productId, req, currentTime);
        } finally {
            //unlock
            lettuceLockRepository.unlock(key);
        }
    }
}
