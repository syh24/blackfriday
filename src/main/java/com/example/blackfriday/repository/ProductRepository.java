package com.example.blackfriday.repository;

import com.example.blackfriday.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
