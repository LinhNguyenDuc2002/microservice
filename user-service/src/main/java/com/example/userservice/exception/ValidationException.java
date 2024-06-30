package com.example.userservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class ValidationException extends Exception{
    private Object errorObject;

    private String message;
}
