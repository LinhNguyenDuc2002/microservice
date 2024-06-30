package com.example.productservice.exception;

import lombok.Getter;

@Getter
public class InvalidException extends Exception {
    public InvalidException(String message) {
        super(message);
    }
}
