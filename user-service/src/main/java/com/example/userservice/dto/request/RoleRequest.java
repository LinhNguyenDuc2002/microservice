package com.example.userservice.dto.request;

import com.example.userservice.constant.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RoleRequest {
    private String description;

    @NotNull(message = "{error.not-null}")
    @NotEmpty(message = "{error.not-empty}")
    private List<String> permissions;
}
