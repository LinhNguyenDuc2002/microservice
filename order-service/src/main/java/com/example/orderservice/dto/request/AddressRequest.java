package com.example.orderservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressRequest {
    private String detail;

    private String ward;

    @NotBlank(message = "{error.not-blank}")
    private String district;

    @NotBlank(message = "{error.not-blank}")
    private String city;

    @NotBlank(message = "{error.not-blank}")
    private String country;
}
