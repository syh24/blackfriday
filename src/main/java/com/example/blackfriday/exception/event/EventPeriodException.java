package com.example.blackfriday.exception.event;

import com.example.blackfriday.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EventPeriodException extends CustomException {
    public EventPeriodException(String message) { super(message); }
}
