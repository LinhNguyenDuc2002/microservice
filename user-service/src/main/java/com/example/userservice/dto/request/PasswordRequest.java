package com.example.userservice.dto.request;

import com.example.servicefoundation.annotation.password.ValidPassword;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @ValidPassword(message = "{error.password.invalid}")
    private String newPassword;
}
