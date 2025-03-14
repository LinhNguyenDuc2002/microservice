package com.example.productservice.payload.orderservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class CheckingDetailResponse {
    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("account_id")
    private String accountId;
}
