package com.example.orderservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReceiverRequest {
    @NotBlank(message = "{error.not-blank}")
    private String name;

    @NotBlank(message = "{error.not-blank}")
    private String phoneNumber;

    @NotNull(message = "{error.not-null}")
    private AddressRequest address;
}
