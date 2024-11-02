package com.example.userservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UpdateInfo {
    @JsonProperty("first_name")
    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String firstName;

    @JsonProperty("last_name")
    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String lastName;

    @JsonProperty("birthday")
    @NotNull(message = "{error.not-null}")
    private Date dob;

    @JsonProperty("sex")
    @NotNull(message = "{error.not-null}")
    private Boolean sex;

    @JsonProperty("address")
    @NotNull(message = "{error.not-null}")
    private AddressRequest addressRequest;
}
