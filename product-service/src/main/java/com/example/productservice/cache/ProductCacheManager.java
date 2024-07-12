package com.example.productservice.cache;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.payload.response.PageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProductCacheManager {
    void clear();
    void saveAllProducts(PageResponse<ProductDTO> products, Integer page, Integer size, String shop, String category) throws JsonProcessingException;

    PageResponse<ProductDTO> getAllProducts(Integer page, Integer size, String shop, String category) throws JsonProcessingException;
}
