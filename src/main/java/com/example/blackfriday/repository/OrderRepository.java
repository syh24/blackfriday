package com.example.blackfriday.repository;

import com.example.blackfriday.domain.Event;
import com.example.blackfriday.domain.Order;
import com.example.blackfriday.domain.Product;
import com.example.blackfriday.repository.dto.CountQuantityByOrderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsOrderByEventAndProduct(Event event, Product product);

    int countOrderByEventAndProduct(Event event, Product product);

    @Query(value = """
            select new com.example.blackfriday.repository.dto.CountQuantityByOrderDto(o.event, o.product, count(o))
            from Order o where Date(o.createdAt) = CURRENT_DATE() and o.updateQuantityFlag = false
            group by o.product, o.event
        """
    )
    List<CountQuantityByOrderDto> findAllByTodayOrderList();

    @Query(value = """
            select o
            from Order o where Date(o.createdAt) = CURRENT_DATE() and o.updateQuantityFlag = false
        """
    )
    List<Order> countAllByTodayOrderList();
}
