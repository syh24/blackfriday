package com.example.blackfriday.exception.member;

import com.example.blackfriday.utils.ApiUtil;
import com.example.blackfriday.utils.ApiUtil.ApiErrorResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.blackfriday.utils.ApiUtil.error;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class MemberExceptionHandler {
    @ExceptionHandler(value = {
            MemberNotFoundException.class,
    })
    public ResponseEntity<ApiErrorResult<String>> notFoundException(Exception e, HttpServletRequest req) {
        log.error("Http Method: {}, URI: {}, message: {}, status: {}", req.getMethod(), req.getRequestURI(), e.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(NOT_FOUND).body(error(NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(value = {
            MemberBadRequestException.class,
    })
    public ResponseEntity<ApiErrorResult<String>> badRequestException(Exception e, HttpServletRequest req) {
        log.error("Http Method: {}, URI: {}, message: {}, status: {}", req.getMethod(), req.getRequestURI(), e.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(BAD_REQUEST).body(error(BAD_REQUEST, e.getMessage()));
    }
}
