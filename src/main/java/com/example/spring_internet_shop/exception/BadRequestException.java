package com.example.spring_internet_shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends ClientError {
    public BadRequestException(String message) {
        super(message);
    }
}
