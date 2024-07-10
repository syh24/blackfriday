package com.example.blackfriday.service;

import com.example.blackfriday.controller.dto.EventProductMessageDto;
import com.example.blackfriday.domain.*;
import com.example.blackfriday.domain.redis.EventProductRedis;
import com.example.blackfriday.exception.event.EventNotFountException;
import com.example.blackfriday.exception.event.EventProductNotFoundException;
import com.example.blackfriday.exception.member.MemberNotFoundException;
import com.example.blackfriday.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final EventProductRepository eventProductRepository;

    @Transactional
    public void createOrder(EventProductMessageDto message) {
        Member member = memberRepository.findById(message.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다."));

        EventProduct eventProduct = eventProductRepository.findById(message.getEventProductId())
                .orElseThrow(() -> new EventProductNotFoundException("해당 이벤트 상품이 존재하지 않습니다."));

        //주문 생성
        orderRepository.save(Order.builder()
                .orderPrice(eventProduct.getEventPrice())
                .quantity(1)
                .updateQuantityFlag(false)
                .product(eventProduct.getProduct())
                .member(member)
                .event(eventProduct.getEvent())
                .build());
    }

}
