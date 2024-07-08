package com.example.blackfriday.service.EventProduct;

import com.example.blackfriday.controller.dto.OrderDto;

import java.time.LocalDateTime;

public interface EventProductService {
    void processEventProduct(OrderDto.EventOrderRequest req, LocalDateTime currentTime) throws InterruptedException;
}
