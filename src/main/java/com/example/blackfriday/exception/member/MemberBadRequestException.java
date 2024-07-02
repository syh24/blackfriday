package com.example.blackfriday.exception.member;

import com.example.blackfriday.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MemberBadRequestException extends CustomException {
    public MemberBadRequestException(String message) {super(message);}
}
