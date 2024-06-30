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

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    @JsonProperty("username")
    @Size(min = 1, message = "Username cannot be empty")
    @NotNull(message = "Username cannot be null")
    private String username;

    @JsonProperty("nickname")
    @Size(min = 1, message = "Nickname cannot be empty")
    @NotNull(message = "Nickname cannot be null")
    private String nickname;

    @JsonProperty("password")
    @Size(min = 6, message = "Password has a minimum length of 6 characters")
    @NotNull(message = "Password cannot be null")
    private String password;

    @JsonProperty("fullname")
    @Size(min = 1, message = "Fullname cannot be empty")
    @NotNull(message = "Fullname cannot be null")
    private String fullname;

    @JsonProperty("birthday")
    @NotNull(message = "Birthday cannot be null")
    private Date dob;

    @JsonProperty("email")
    @Size(min = 1, message = "Email cannot be empty")
    @NotNull(message = "Email cannot be null")
    private String email;

    @JsonProperty("phone")
    @Size(min = 1, message = "Phone cannot be empty")
    @NotNull(message = "Phone cannot be null")
    private String phone;

    @JsonProperty("role")
    @NotNull(message = "Role cannot be null")
    private String role;
}
