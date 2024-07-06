package com.example.blackfriday.service.EventProduct;

import com.example.blackfriday.controller.dto.OrderDto;

import java.time.LocalDateTime;

public interface EventProductService {
    void processEventProduct(OrderDto.OrderRequest req, Long productId, LocalDateTime currentTime) throws InterruptedException;
}
