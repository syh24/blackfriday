package com.example.blackfriday.controller;

import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.service.EventProduct.EventProductService;
import com.example.blackfriday.utils.ApiUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final EventProductService service;

    @PostMapping("/eventProducts/{id}")
    public ApiUtil.ApiSuccessResult<String> createEventOrder(
            @PathVariable(name = "id") Long eventProductId,
            @RequestBody @Valid OrderDto.EventOrderRequest request
            ) throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        service.processEventProduct(request, eventProductId, now);
        return ApiUtil.success("이벤트 주문이 생성되었습니다.");
    }
}
