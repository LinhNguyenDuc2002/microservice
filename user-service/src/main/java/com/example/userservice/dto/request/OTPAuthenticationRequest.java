package com.example.userservice.dto.request;

import lombok.Getter;

@Getter
public class OTPAuthenticationRequest {
    private String secret;

    private String otp;
}
