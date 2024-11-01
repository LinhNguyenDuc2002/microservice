package com.example.userservice.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Status {
    SUCCESS("status.success"),

    FAIL("status.fail");

    private String message;
}
