package com.example.blackfriday.service;

import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.domain.*;
import com.example.blackfriday.exception.event.EventNotFountException;
import com.example.blackfriday.exception.member.MemberNotFoundException;
import com.example.blackfriday.exception.product.ProductNotFoundException;
import com.example.blackfriday.repository.EventRepository;
import com.example.blackfriday.repository.MemberRepository;
import com.example.blackfriday.repository.OrderRepository;
import com.example.blackfriday.repository.ProductRepository;
import com.example.blackfriday.service.EventProduct.EventProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final EventRepository eventRepository;
    private final EventProductService eventProductService;

    /**
     * Lettuce를 사용한 동시성 처리
     */
    public void createEventOrder(OrderDto.OrderRequest request, Long productId, LocalDateTime now) throws InterruptedException {
        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));
        Event event = eventRepository.findById(request.getEventId()).orElseThrow(() -> new EventNotFountException("해당 이벤트를 찾을 수 없습니다."));

        //이벤트 상품 수량 감소
        eventProductService.getEventProduct(request, productId, now);

//        orderRepository.save(Order.builder()
//                .orderPrice(eventProduct.getEventPrice())
//                .quantity(request.getQuantity())
//                .event(event)
//                .member(member)
//                .product(product)
//                .build());
    }
}
