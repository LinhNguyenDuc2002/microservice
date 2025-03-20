package com.example.productservice.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubTypeRequest {
    @NotNull(message = "{error.not-null}")
    private String name;

    @NotNull(message = "{error.not-null}")
    @Min(value = 0, message = "{error.quantity.negative}")
    @Max(value = Long.MAX_VALUE, message = "{error.quantity.max}")
    private Double price;

    @NotNull(message = "{error.not-null}")
    @Min(value = 0, message = "{error.quantity.negative}")
    @Max(value = Integer.MAX_VALUE, message = "{error.quantity.max}")
    private Integer quantity;
}