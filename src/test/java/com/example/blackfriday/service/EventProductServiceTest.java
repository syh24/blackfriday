package com.example.blackfriday.service;

import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.domain.*;
import com.example.blackfriday.repository.*;
import com.example.blackfriday.service.EventProduct.EventProductServiceV3Impl;
import com.example.blackfriday.service.async.AsyncEventProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventProductServiceTest {

//    @Autowired
//    private EventProductServiceV1Impl service;

//    @Autowired
//    private EventProductServiceV2Impl service;

    @Autowired
    private EventProductServiceV3Impl service;

    @Autowired
    private AsyncEventProductService asyncService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EventProductRepository eventProductRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Product product;
    private Event event;
    private EventProduct eventProduct;
    private Member member;


    public Member createMember() {
        return Member.builder()
                .name("서윤혁")
                .phone("010-1234-1234")
                .email("test@gmail.com")
                .password("abc1234")
                .role(Role.ROLE_USER)
                .build();
    }

    public Product createProduct() {
        return Product.builder()
                .category("상의")
                .title("셔츠")
                .description("소라색 셔츠")
                .price(30000)
                .build();
    }

    public Event createEvent() {
        return Event.builder()
                .category("블랙프라이데이")
                .description("블프 기념 할인 한정 수량 이벤트")
                .startDate(LocalDate.of(2024, 7,1))
                .endDate(LocalDate.of(2024, 7,20))
                .eventStartTime("00:00:00")
                .eventEndTime("23:00:00")
                .build();
    }

    public EventProduct createEventProduct(Product product, Event event) {
        return EventProduct.builder()
                .product(product)
                .event(event)
                .eventPrice(20000)
                .eventQuantity(500)
                .build();
    }

    public OrderDto.EventOrderRequest createOrderRequest(Long eventId, Long memberId) {
        return OrderDto.EventOrderRequest.builder()
                .memberId(memberId)
                .eventId(eventId)
                .build();
    }

    @BeforeEach
    void setUp() {
        member = memberRepository.save(createMember());
        product = productRepository.save(createProduct());
        event = eventRepository.save(createEvent());
        eventProduct = eventProductRepository.save(createEventProduct(product, event));
    }


    @Test
    void 이벤트_상품_동시성_테스트() throws Exception {
        int numberOfThreads = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
               try {
                   OrderDto.EventOrderRequest req = createOrderRequest(event.getId(), member.getId());
                   service.processEventProduct(req, eventProduct.getId(), LocalDateTime.now());
               } catch (Exception e) {
                   throw new RuntimeException(e);
               } finally {
                   latch.countDown();
               }
            });
        }
        latch.await();

        EventProduct findEventProduct = eventProductRepository.findById(eventProduct.getId()).get();
        assertEquals(400, findEventProduct.getEventQuantity());
    }

    @Test
    void 비동기_이벤트_상품_동시성_테스트() throws Exception {
        int numberOfThreads = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    OrderDto.EventOrderRequest req = createOrderRequest(event.getId(), member.getId());
                    asyncService.processEventProduct(req, eventProduct.getId(), LocalDateTime.now());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Thread.sleep(10000);

        assertEquals(500, orderRepository.countOrderByEventAndProduct(event, product));
    }
}