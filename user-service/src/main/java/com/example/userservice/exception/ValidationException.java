package com.example.userservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ValidationException extends Exception{
    private Object errorObject;

    private String message;

    public ValidationException(Object errorObject, String message) {
        this.errorObject = errorObject;
        this.message = message;
    }

    public ValidationException(String message) {
        this.message = message;
    }
}
