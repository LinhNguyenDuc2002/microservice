package com.example.productservice.dto.request;

import com.example.productservice.entity.ProductDetail;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductRequest {
    @NotBlank(message = "{error.not-blank}")
    private String name;

    private String description;

    @NotBlank(message = "{error.not-blank}")
    private String categoryId;

    @NotNull(message = "{error.not-null}")
    @NotEmpty(message = "{error.not-empty}")
    private List<ProductDetailRequest> productDetails;

    @NotNull(message = "{error.not-null}")
    @NotEmpty(message = "{error.not-empty}")
    private List<MultipartFile> images;
}
