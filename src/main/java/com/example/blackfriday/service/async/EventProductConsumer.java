package com.example.blackfriday.service.async;

import com.example.blackfriday.controller.dto.EventProductMessageDto;
import com.example.blackfriday.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventProductConsumer {

    private final OrderService orderService;
    private final UpdateEventProductService updateEventProductService;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeEventProductMessage(EventProductMessageDto message) {
        log.info("이벤트 상품 요청 처리 memberId: {}, eventProductId: {}", message.getMemberId(), message.getEventProductId());
        orderService.createOrder(message);
    }

//    @Scheduled(fixedDelay = 10000)
    private void updateEventProductQuantity() {
        log.info("이벤트 상품 재고 동기화");
        updateEventProductService.synchronizedQuantity();
    }
}
