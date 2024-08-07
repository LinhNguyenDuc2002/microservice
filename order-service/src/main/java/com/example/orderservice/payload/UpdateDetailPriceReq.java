package com.example.orderservice.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateDetailPriceReq {
    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("product_type_id")
    private String productTypeId;

    @JsonProperty("price")
    private Double price;
}
