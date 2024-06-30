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
public class AddressRequest {
    @JsonProperty("specific_address")
    private String specificAddress;

    private String ward;

    @Size(min = 1, message = "District cannot be empty")
    @NotNull(message = "District cannot be null")
    private String district;

    @Size(min = 1, message = "City name cannot be empty")
    @NotNull(message = "City name cannot be null")
    private String city;

    @Size(min = 1, message = "Country cannot be empty")
    @NotNull(message = "Country cannot be null")
    private String country;
}
