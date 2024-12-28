package com.example.productservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Response<T> {
    @JsonProperty("status")
    private String status;

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("data")
    private T data;

    @JsonProperty("message")
    private String message;
}
