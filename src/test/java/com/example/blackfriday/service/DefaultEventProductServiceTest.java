package com.example.blackfriday.service;

import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.domain.*;
import com.example.blackfriday.exception.event.EventAlreadyParticipationException;
import com.example.blackfriday.exception.event.EventPeriodException;
import com.example.blackfriday.exception.member.MemberBadRequestException;
import com.example.blackfriday.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
class DefaultEventProductServiceTest {

    @InjectMocks
    DefaultEventProductService defaultEventProductService;

    @Mock
    MemberRepository memberRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    EventRepository eventRepository;
    @Mock
    EventProductRepository eventProductRepository;

    public Member createMember() {
        return Member.builder()
                .id(1L)
                .name("서윤혁")
                .phone("010-1234-1234")
                .email("test@gmail.com")
                .password("abc1234")
                .role(Role.ROLE_USER)
                .build();
    }

    public Product createProduct() {
        return Product.builder()
                .id(1L)
                .category("상의")
                .title("셔츠")
                .description("소라색 셔츠")
                .price(30000)
                .build();
    }

    public Event createEventForDateSuccessTest() {
        return Event.builder()
                .id(1L)
                .category("블랙프라이데이")
                .description("블프 기념 할인 한정 수량 이벤트")
                .startDate(LocalDate.of(2024, 7,1))
                .endDate(LocalDate.of(2024, 7,20))
                .eventStartTime("10:00:00")
                .eventEndTime("20:00:00")
                .build();
    }

    public Event createEventForDateFailTest() {
        return Event.builder()
                .id(2L)
                .category("블랙프라이데이")
                .description("블프 기념 할인 한정 수량 이벤트")
                .startDate(LocalDate.of(2024, 8,1))
                .endDate(LocalDate.of(2024, 8,20))
                .eventStartTime("18:00:00")
                .eventEndTime("20:00:00")
                .build();
    }

    public EventProduct createEventProduct(Product product, Event event) {
        return EventProduct.builder()
                .id(1L)
                .product(product)
                .event(event)
                .eventPrice(20000)
                .eventQuantity(500)
                .build();
    }

    public OrderDto.OrderRequest createOrderRequest(Long eventId, Long memberId) {
        return OrderDto.OrderRequest.builder()
                .memberId(memberId)
                .eventId(eventId)
                .build();
    }

    @Test
    @DisplayName("재고 감소 성공")
    void eventProductQuantityTest() {
        Member member = createMember();
        Product product = createProduct();
        Event event = createEventForDateSuccessTest();
        EventProduct eventProduct = createEventProduct(product, event);


        when(orderRepository.existsOrderByEventAndProduct(any(), any())).thenReturn(false);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.ofNullable(member));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.ofNullable(event));
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(product));
        when(eventProductRepository.findEventProductByEventAndProduct(any(), any())).thenReturn(Optional.ofNullable(eventProduct));

        defaultEventProductService.decreaseQuantity(product.getId(),createOrderRequest(event.getId(), member.getId()), LocalDateTime.now());

        assertEquals(499, eventProduct.getEventQuantity());
    }

    @Test
    @DisplayName("이벤트 날짜 실패")
    void eventPeriodTest() {
        Member member = createMember();
        Product product = createProduct();
        Event event = createEventForDateFailTest();
        EventProduct eventProduct = createEventProduct(product, event);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.ofNullable(member));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.ofNullable(event));
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(product));

        Assertions.assertThatThrownBy(() -> defaultEventProductService.decreaseQuantity(product.getId(),createOrderRequest(event.getId(), member.getId()), LocalDateTime.now()))
                .isInstanceOf(EventPeriodException.class)
                .hasMessageStartingWith("이벤트 기간이 아닙니다.");
    }

    @Test
    @DisplayName("이벤트 중복 참여 테스트")
    void eventDupTest() {
        Member member = createMember();
        Product product = createProduct();
        Event event = createEventForDateSuccessTest();
        EventProduct eventProduct = createEventProduct(product, event);


        when(orderRepository.existsOrderByEventAndProduct(any(), any())).thenReturn(true);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.ofNullable(member));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.ofNullable(event));
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(product));
        when(eventProductRepository.findEventProductByEventAndProduct(any(), any())).thenReturn(Optional.ofNullable(eventProduct));

        Assertions.assertThatThrownBy(() -> defaultEventProductService.decreaseQuantity(product.getId(),createOrderRequest(event.getId(), member.getId()), LocalDateTime.now()))
                .isInstanceOf(EventAlreadyParticipationException.class)
                .hasMessageStartingWith("이벤트를 중복 참여하실 수 없습니다.");
    }
}