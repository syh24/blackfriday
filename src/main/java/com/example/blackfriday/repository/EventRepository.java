package com.example.blackfriday.repository;

import com.example.blackfriday.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
