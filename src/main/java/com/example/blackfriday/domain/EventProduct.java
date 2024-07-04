package com.example.blackfriday.domain;

import com.example.blackfriday.exception.event.EventProductQuantityException;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class EventProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_product_id")
    private Long id;
    private int eventPrice;
    private int eventQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    public void decreaseEventQuantity() {
        validateQuantity();
        this.eventQuantity -= 1;
    }

    public void validateQuantity() {
        if (this.eventQuantity < 1)
            throw new EventProductQuantityException("이벤트 상품 수량이 부족합니다.");
    }

}
