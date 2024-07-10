package com.example.blackfriday.service.async;

import com.example.blackfriday.domain.EventProduct;
import com.example.blackfriday.domain.Order;
import com.example.blackfriday.exception.event.EventProductNotFoundException;
import com.example.blackfriday.repository.EventProductRepository;
import com.example.blackfriday.repository.OrderRepository;
import com.example.blackfriday.repository.dto.CountQuantityByOrderDto;
import com.example.blackfriday.service.redis.RedissonLockHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateEventProductService {

    private final OrderRepository orderRepository;
    private final EventProductRepository eventProductRepository;

    public void synchronizedQuantity() {
        List<Order> orderList = orderRepository.countAllByTodayOrderList();
        List<CountQuantityByOrderDto> orders = orderRepository.findAllByTodayOrderList();
        if ((long) orderList.size() != orders.stream().mapToLong(CountQuantityByOrderDto::getCount).sum()) {
            throw new IllegalStateException("주문 테이블이 변경되었습니다.");
        }
        orderList.forEach(Order::changeUpdateQuantityFlag);
        orders.forEach(this::updateQuantity);
    }

    private void updateQuantity(CountQuantityByOrderDto order) {
        EventProduct eventProduct = eventProductRepository.findEventProductByEventAndProduct(order.getEvent(), order.getProduct())
                .orElseThrow(() -> new EventProductNotFoundException("해당 이벤트 상품을 찾을 수 없습니다."));

        eventProduct.decreaseEventQuantityByScheduler(order.getCount());
    }
}
