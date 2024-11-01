package com.example.userservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class InvalidationException extends Exception{
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
