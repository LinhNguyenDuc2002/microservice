package com.example.userservice.dto.request;

import com.example.servicefoundation.annotation.email.ValidEmail;
import com.example.servicefoundation.annotation.password.ValidPassword;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserRegistration {
    @JsonProperty("username")
    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String username;

    @JsonProperty("password")
    @ValidPassword(message = "{error.password.invalid}")
    private String password;

    @JsonProperty("email")
    @ValidEmail(message = "{error.email.invalid}")
    private String email;

    @JsonProperty("phone")
    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String phone;
}
