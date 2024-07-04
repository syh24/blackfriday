package com.example.blackfriday.repository;

import com.example.blackfriday.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
