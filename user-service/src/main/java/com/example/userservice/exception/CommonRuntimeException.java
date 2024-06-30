package com.example.userservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class CommonRuntimeException extends RuntimeException {
    private HttpStatus status;

    private Object errorObject;

    private String message;
}
