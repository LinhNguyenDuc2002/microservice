package com.example.productservice.mapper;

import com.example.productservice.dto.ProductDetailDTO;
import com.example.productservice.entity.ProductDetail;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailMapper extends AbstractMapper<ProductDetail, ProductDetailDTO> {
    @Override
    public Class<ProductDetailDTO> getDtoClass() {
        return ProductDetailDTO.class;
    }

    @Override
    public Class<ProductDetail> getEntityClass() {
        return ProductDetail.class;
    }
}
