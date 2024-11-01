package com.example.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PermissionRequest {
    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String code;

    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String name;

    private String description;
}
