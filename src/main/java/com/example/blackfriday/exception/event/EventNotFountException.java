package com.example.blackfriday.exception.event;

import com.example.blackfriday.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EventNotFountException extends CustomException {
    public EventNotFountException(String message) { super(message); }
}
