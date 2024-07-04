package com.example.blackfriday.exception.member;

import com.example.blackfriday.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MemberNotFoundException extends CustomException {
    public MemberNotFoundException(String message) { super(message); }
}
