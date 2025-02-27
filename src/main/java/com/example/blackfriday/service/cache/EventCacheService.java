package com.example.blackfriday.service.cache;

import com.example.blackfriday.domain.Event;
import com.example.blackfriday.domain.redis.EventRedis;
import com.example.blackfriday.exception.event.EventNotFountException;
import com.example.blackfriday.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventCacheService {

    private final EventRepository eventRepository;

    @Cacheable(key = "#eventId", value = "event")
    public EventRedis getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFountException("해당 이벤트를 찾을 수 없습니다."));
        return new EventRedis(event);
    }
}
