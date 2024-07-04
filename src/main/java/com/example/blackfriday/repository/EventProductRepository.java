package com.example.blackfriday.repository;

import com.example.blackfriday.domain.Event;
import com.example.blackfriday.domain.EventProduct;
import com.example.blackfriday.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EventProductRepository extends JpaRepository<EventProduct, Long> {

    @Query("select ep from EventProduct ep where ep.event.id = :eventId and ep.product.id = :productId")
    Optional<EventProduct> findEventProductByEventAndProduct(Long eventId, Long productId);
}
