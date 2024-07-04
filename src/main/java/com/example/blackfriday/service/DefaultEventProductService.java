package com.example.blackfriday.service;

import com.example.blackfriday.domain.EventProduct;
import com.example.blackfriday.exception.event.EventProductNotFoundException;
import com.example.blackfriday.repository.EventProductRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultEventProductService {

    private final EventProductRepository eventProductRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseQuantity(Long productId, Long eventId) {
        EventProduct eventProduct = eventProductRepository.findEventProductByEventAndProduct(eventId, productId)
                .orElseThrow(() -> new EventProductNotFoundException("해당 이벤트 상품이 존재하지 않습니다."));

        eventProduct.decreaseEventQuantity();
    }
}
