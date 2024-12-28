package com.example.productservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class WareHouseCheckingDto {
    private String status;

    private List<ProductDetailItem> productDetailItems;

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @Builder
    public static class ProductDetailItem {
        private boolean exist;

        private String productDetailId;

        private String info;
    }
}
