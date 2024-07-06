package com.example.blackfriday.repository;

import com.example.blackfriday.domain.Event;
import com.example.blackfriday.domain.Order;
import com.example.blackfriday.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsOrderByEventAndProduct(Event event, Product product);
}
