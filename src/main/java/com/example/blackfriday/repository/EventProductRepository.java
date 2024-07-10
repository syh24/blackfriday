package com.example.blackfriday.repository;

import com.example.blackfriday.domain.Event;
import com.example.blackfriday.domain.EventProduct;
import com.example.blackfriday.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EventProductRepository extends JpaRepository<EventProduct, Long> {

    Optional<EventProduct> findEventProductByEventAndProduct(Event event, Product product);
}
