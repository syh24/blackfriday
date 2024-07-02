package com.example.blackfriday.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomException extends RuntimeException {
    protected CustomException(String message) {super(message);}
}
