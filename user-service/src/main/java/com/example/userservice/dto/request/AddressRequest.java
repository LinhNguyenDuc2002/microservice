package com.example.userservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
public class AddressRequest {
    @JsonProperty("specific_address")
    private String specificAddress;

    private String ward;

    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String district;

    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String city;

    @NotNull(message = "{error.not-null}")
    @NotBlank(message = "{error.not-blank}")
    private String country;
}
