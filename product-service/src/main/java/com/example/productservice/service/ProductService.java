package com.example.productservice.service;

import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.ProductDetailDTO;
import com.example.productservice.dto.request.BasicProductRequest;
import com.example.productservice.dto.request.ProductFormRequest;
import com.example.servicefoundation.exception.I18nException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductDTO add(ProductFormRequest productFormRequest) throws IOException, I18nException;

    ProductDTO update(String id, BasicProductRequest productRequest) throws IOException, I18nException;

    PageDTO<ProductDTO> getAll(Integer page, Integer size, String search, String category, List<String> sortColumns) throws JsonProcessingException;

    ProductDetailDTO get(String id) throws I18nException;

    void delete(String id) throws I18nException;
}
