package com.example.blackfriday.config.component;

import com.example.blackfriday.controller.dto.EventProductMessageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RabbitMqConsumerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    @DisplayName("Rabbit Queue 메시지 수신")
    void recieveQueueMessage() {
        EventProductMessageDto message = (EventProductMessageDto) rabbitTemplate.receiveAndConvert("event-product.queue");
        System.out.println(message);
    }
}