package com.example.blackfriday.service;

import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.domain.*;
import com.example.blackfriday.exception.event.EventNotFountException;
import com.example.blackfriday.exception.event.EventPeriodException;
import com.example.blackfriday.exception.event.EventProductNotFoundException;
import com.example.blackfriday.exception.member.MemberNotFoundException;
import com.example.blackfriday.exception.product.ProductNotFoundException;
import com.example.blackfriday.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DefaultEventProductService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final EventProductRepository eventProductRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseQuantity(Long productId, OrderDto.OrderRequest req, LocalDateTime currentTime) {
        Member member = memberRepository.findById(req.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));
        Event event = eventRepository.findById(req.getEventId())
                .orElseThrow(() -> new EventNotFountException("해당 이벤트를 찾을 수 없습니다."));


        //이벤트 날짜 확인
        if (!checkValidEvent(event, currentTime)) {
            throw new EventPeriodException("이벤트 기간이 아닙니다.");
        }

        EventProduct eventProduct = eventProductRepository.findEventProductByEventAndProduct(req.getEventId(), productId)
                .orElseThrow(() -> new EventProductNotFoundException("해당 이벤트 상품이 존재하지 않습니다."));

        //이벤트 상품 재고 감소
        eventProduct.decreaseEventQuantity();

        //주문 생성
        orderRepository.save(
                Order.builder()
                        .product(product)
                        .quantity(1)
                        .event(event)
                        .member(member)
                        .build());
    }

    private boolean checkEventDate(Event event, LocalDateTime currentTime) {
        LocalDate currentDate = currentTime.toLocalDate();

        Period startPeriod = Period.between(event.getStartDate(), currentDate);
        Period endPeriod = Period.between(currentDate, event.getEndDate());

        return (startPeriod.isZero() || !startPeriod.isNegative()) && (endPeriod.isZero() || endPeriod.isNegative());
    }


    private boolean checkEventTime(Event event, LocalDateTime currentTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalTime startTime = LocalTime.parse(event.getEventStartTime(), formatter);
        LocalTime endTime = LocalTime.parse(event.getEventEndTime(), formatter);

        Duration startDuration = Duration.between(startTime, currentTime.toLocalTime());
        Duration endDuration = Duration.between(currentTime.toLocalTime(), endTime);

        return (startDuration.isZero() || !startDuration.isNegative()) && (endDuration.isZero() || endDuration.isNegative());
    }

    private boolean checkValidEvent(Event event, LocalDateTime currentTime) {
        return checkEventDate(event, currentTime) || checkEventTime(event, currentTime);
    }
}
