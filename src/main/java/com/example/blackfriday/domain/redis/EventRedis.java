package com.example.blackfriday.domain.redis;

import com.example.blackfriday.domain.Event;
import com.example.blackfriday.exception.event.EventPeriodException;
import com.example.blackfriday.exception.event.EventTimeException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.*;
import java.time.format.DateTimeFormatter;

@RedisHash("event")
public record EventRedis (

    @Id
    Long eventId,
    String category,
    String description,
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate startDate,
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate endDate,
    String eventStartTime,
    String eventEndTime

) {
    public EventRedis(Event event) {
        this (
                event.getId(),
                event.getCategory(),
                event.getDescription(),
                event.getStartDate(),
                event.getEndDate(),
                event.getEventStartTime(),
                event.getEventEndTime()
        );
    }

    private boolean checkEventDate(LocalDateTime currentTime) {
        LocalDate currentDate = currentTime.toLocalDate();

        Period startPeriod = Period.between(startDate, currentDate);
        Period endPeriod = Period.between(currentDate, endDate);

        return (startPeriod.isZero() || !startPeriod.isNegative()) && (endPeriod.isZero() || !endPeriod.isNegative());
    }


    private boolean checkEventTime(LocalDateTime currentTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalTime startTime = LocalTime.parse(eventStartTime, formatter);
        LocalTime endTime = LocalTime.parse(eventEndTime, formatter);

        Duration startDuration = Duration.between(startTime, currentTime.toLocalTime());
        Duration endDuration = Duration.between(currentTime.toLocalTime(), endTime);

        return (startDuration.isZero() || !startDuration.isNegative()) && (endDuration.isZero() || !endDuration.isNegative());
    }

    public void checkValidEvent(LocalDateTime currentTime) {
        if (!checkEventDate(currentTime)) {
            throw new EventPeriodException("이벤트 기간이 아닙니다.");
        }
        if (!checkEventTime(currentTime)) {
            throw new EventTimeException("이벤트 시간이 아닙니다.");
        }
    }
}
