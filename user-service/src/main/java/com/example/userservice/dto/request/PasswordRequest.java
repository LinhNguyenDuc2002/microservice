package com.example.userservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PasswordRequest {
    @JsonProperty("old-password")
    private String oldPassword;

    @JsonProperty("new-password")
    @Size(min = 6, message = "Password has a minimum length of 6 characters")
    @NotNull(message = "Password cannot be null")
    private String newPassword;
}
