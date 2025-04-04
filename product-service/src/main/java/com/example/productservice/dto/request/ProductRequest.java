package com.example.productservice.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductRequest {
    @NotBlank(message = "{error.not-blank}")
    private String name;

    @NotNull(message = "{error.not-null}")
    @NotEmpty(message = "{error.not-empty}")
    private List<MultipartFile> images;

    private Double price;

    private Integer quantity;

    private String description;

    @NotBlank(message = "{error.not-blank}")
    private String categoryId;

    private List<ProductTypeRequest> productTypes;
}