package com.example.productservice.mapper;

import com.example.productservice.dto.ProductTypeDTO;
import com.example.productservice.entity.ProductType;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeMapper extends AbstractMapper<ProductType, ProductTypeDTO> {
    @Override
    public Class<ProductTypeDTO> getDtoClass() {
        return ProductTypeDTO.class;
    }

    @Override
    public Class<ProductType> getEntityClass() {
        return ProductType.class;
    }
}
