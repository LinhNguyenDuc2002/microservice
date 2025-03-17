package com.example.productservice.mapper;

import com.example.productservice.dto.ProductDetailDTO;
import com.example.productservice.entity.ProductType;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailMapper extends AbstractMapper<ProductType, ProductDetailDTO> {
    @Override
    public Class<ProductDetailDTO> getDtoClass() {
        return ProductDetailDTO.class;
    }

    @Override
    public Class<ProductType> getEntityClass() {
        return ProductType.class;
    }
}
