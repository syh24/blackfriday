package com.example.blackfriday.repository.dto;

import com.example.blackfriday.domain.Event;
import com.example.blackfriday.domain.Product;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountQuantityByOrderDto {
    private Event event;
    private Product product;
    private long count;
}
