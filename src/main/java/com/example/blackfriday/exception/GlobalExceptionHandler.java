package com.example.blackfriday.exception;

import com.example.blackfriday.utils.ApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        logger.error(e.getMessage(), e);
        ApiUtil.ApiErrorResult<String> error = ApiUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류 발생");
        return super.handleExceptionInternal(e, error, headers, statusCode, request);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.status(BAD_REQUEST).body(ApiUtil.error(BAD_REQUEST, "Method Argument Not Valid"));
    }

}
