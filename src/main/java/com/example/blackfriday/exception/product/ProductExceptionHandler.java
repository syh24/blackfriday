package com.example.blackfriday.exception.product;

import com.example.blackfriday.exception.member.MemberNotFoundException;
import com.example.blackfriday.utils.ApiUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.blackfriday.utils.ApiUtil.error;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class ProductExceptionHandler {

    @ExceptionHandler(value = {
            ProductNotFoundException.class,
    })
    public ResponseEntity<ApiUtil.ApiErrorResult<String>> notFoundException(Exception e, HttpServletRequest req) {
        log.error("Http Method: {}, URI: {}, message: {}, status: {}", req.getMethod(), req.getRequestURI(), e.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(NOT_FOUND).body(error(NOT_FOUND, e.getMessage()));
    }
}
