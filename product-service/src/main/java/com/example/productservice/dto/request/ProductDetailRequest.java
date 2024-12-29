package com.example.productservice.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDetailRequest {
    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String name;

    private String description;

    @NotNull(message = "{error.not-null}")
    @Min(value = 0, message = "{error.quantity.negative}")
    @Max(value = Integer.MAX_VALUE, message = "{error.quantity.max}")
    private Integer quantity;

    @NotNull(message = "{error.not-null}")
    @Min(value = 0, message = "{error.quantity.negative}")
    @Max(value = Long.MAX_VALUE, message = "{error.quantity.max}")
    private Double price;

    @NotNull(message = "{error.not-null}")
    private MultipartFile image;
}
