package com.example.productservice.dto.statistic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductStatisticsDTO {
    @JsonProperty("number_of_product")
    private Long purchasedProduct;

    @JsonProperty("product")
    private List<DetailProductStatisticsDTO> products;
}
