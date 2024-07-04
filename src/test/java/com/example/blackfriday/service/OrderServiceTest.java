package com.example.blackfriday.service;

import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.domain.*;
import com.example.blackfriday.repository.EventProductRepository;
import com.example.blackfriday.repository.EventRepository;
import com.example.blackfriday.repository.ProductRepository;
import com.example.blackfriday.service.EventProduct.EventProductServiceV1Impl;
import com.example.blackfriday.service.EventProduct.EventProductServiceV2Impl;
import com.example.blackfriday.service.EventProduct.EventProductServiceV3Impl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

//    @Autowired
//    private EventProductServiceV1Impl service;

//    @Autowired
//    private EventProductServiceV2Impl service;

    @Autowired
    private EventProductServiceV3Impl service;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EventProductRepository eventProductRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    EntityManager em;

    private Product saveProduct;
    private Event saveEvent;
    private EventProduct saveEventProduct;


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
                .startDate(LocalDate.of(2024, 07,01))
                .endDate(LocalDate.of(2024, 07,20))
                .eventStartTime("18:00")
                .eventEndTime("20:00")
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

    public OrderDto.OrderRequest createOrderRequest(Long eventId) {
        return OrderDto.OrderRequest.builder()
                .eventId(eventId)
                .quantity(1)
                .build();
    }

    @BeforeEach
    void setUp() {
        saveProduct = productRepository.save(createProduct());
        saveEvent = eventRepository.save(createEvent());
        saveEventProduct = eventProductRepository.save(createEventProduct(saveProduct, saveEvent));
    }


    @Test
    void 이벤트_상품_동시성_테스트() throws Exception {
        int numberOfThreads = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
               try {
                   OrderDto.OrderRequest req = createOrderRequest(saveEvent.getId());
                   service.getEventProduct(req, saveProduct.getId(), LocalDateTime.now());
//                   orderService.createEventOrder(req, saveProduct.getId(), LocalDateTime.now());
               } catch (Exception e) {
                   throw new RuntimeException(e);
               } finally {
                   latch.countDown();
               }
            });
        }
        latch.await();

        EventProduct findEventProduct = eventProductRepository.findById(saveEventProduct.getId()).get();
        assertEquals(400, findEventProduct.getEventQuantity());
    }
}