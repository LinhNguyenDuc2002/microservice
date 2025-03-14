package com.example.productservice.cache;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.PageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProductCacheManager {
    void clear();
    void saveAllProducts(PageDTO<ProductDTO> products, Integer page, Integer size, String category) throws JsonProcessingException;

    PageDTO<ProductDTO> getAllProducts(Integer page, Integer size, String category) throws JsonProcessingException;
}
