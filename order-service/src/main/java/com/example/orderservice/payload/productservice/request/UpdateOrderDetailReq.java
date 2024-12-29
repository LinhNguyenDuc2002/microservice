package com.example.orderservice.payload.productservice.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class UpdateOrderDetailReq {
    @NotBlank
    @JsonProperty("product_detail_id")
    private String productDetailId;

    @JsonProperty("price")
    @Min(value = 0)
    private Double price;

    @JsonProperty("price")
    @Min(value = 0)
    private Integer quantity;
}
