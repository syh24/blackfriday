package com.example.blackfriday.config.component;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RabbitMqProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;


    /**
     * exchangeName와 routekey를 사용하여 queue 저장
     * @param object 메시지 오브젝트
     */
    public void producer(Object object) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, object);
    }
}
