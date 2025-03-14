package com.example.productservice.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CommonResponse<T> {
    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;
}
