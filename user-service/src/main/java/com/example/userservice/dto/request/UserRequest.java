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

    @JsonProperty("sex")
    private Boolean sex;

    @JsonProperty("address")
    @NotNull(message = "Address cannot be null")
    private AddressRequest addressRequest;
}
