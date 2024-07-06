package com.example.blackfriday.exception.event;

import com.example.blackfriday.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EventAlreadyParticipationException extends CustomException {
    public EventAlreadyParticipationException(String message)  { super(message); }
}
