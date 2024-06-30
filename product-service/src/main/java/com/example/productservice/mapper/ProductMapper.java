package com.example.productservice.mapper;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.entity.Image;
import com.example.productservice.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper extends AbstractMapper<Product, ProductDTO> {
    @Override
    public ProductDTO toDto(Product product) {
        ProductDTO productDTO = super.toDto(product);

        if(!product.getImages().isEmpty()) {
            productDTO.setImages(product.getImages().stream().map(Image::getUrl).toList());
        }
        productDTO.setCategory(product.getCategory().getName());

        return productDTO;
    }
    @Override
    public Class<ProductDTO> getDtoClass() {
        return ProductDTO.class;
    }

    @Override
    public Class<Product> getEntityClass() {
        return Product.class;
    }
}
