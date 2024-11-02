package com.example.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OTPAuthenticationRequest {
    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String secret;

    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String otp;
}
