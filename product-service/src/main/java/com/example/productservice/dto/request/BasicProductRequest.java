package com.example.productservice.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BasicProductRequest {
    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String name;

    private String description;

    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String categoryId;

    private List<MultipartFile> images;
}
