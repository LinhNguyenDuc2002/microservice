package com.example.productservice.payload.orderservice.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class ProductCheckingRequest {
    @NotBlank
    private String productDetailId;

    @Min(value = 1)
    private Integer quantity;
}
