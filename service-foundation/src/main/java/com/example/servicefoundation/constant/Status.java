package com.example.servicefoundation.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    SUCCESS("Success"),

    FAIL("Fail");

    private String message;
}
