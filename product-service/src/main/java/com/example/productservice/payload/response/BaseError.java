package com.example.productservice.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseError implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("timestamp")
    protected Date timestamp;

    @JsonProperty("error")
    protected String error;
}
