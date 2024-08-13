package com.example.blackfriday.config.security.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum JwtExceptionCode {
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED,"wrong token."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"expired token."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED,"unsupported token."),
    ILLEGAL_TOKEN(HttpStatus.UNAUTHORIZED,"illegal token.");

    private HttpStatus code;
    private String message;

    JwtExceptionCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}
