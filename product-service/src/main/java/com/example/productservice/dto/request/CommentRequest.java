package com.example.productservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentRequest {
    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String message;

    private List<MultipartFile> images;
}
