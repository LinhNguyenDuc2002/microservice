package com.example.userservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class Credentials {
    @JsonProperty("username")
    @NotNull
    @NotBlank
    private String username;

    @JsonProperty("password")
    @NotNull
    @NotBlank
    private String password;
}
