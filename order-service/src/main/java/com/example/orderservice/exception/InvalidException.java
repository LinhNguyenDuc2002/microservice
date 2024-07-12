package com.example.orderservice.exception;

import lombok.Getter;

@Getter
public class InvalidException extends Exception {
    public InvalidException(String message) {
        super(message);
    }
}
