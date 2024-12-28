package com.example.productservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InvalidationException extends Exception {
    private Object errorObject;

    private String message;

    public InvalidationException(Object errorObject, String message) {
        this.errorObject = errorObject;
        this.message = message;
    }

    public InvalidationException(String message) {
        this.message = message;
    }
}
