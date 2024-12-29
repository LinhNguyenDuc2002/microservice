package com.example.productservice.service;

import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.request.BasicProductRequest;
import com.example.productservice.dto.request.ProductRequest;
import com.example.productservice.exception.InvalidationException;
import com.example.productservice.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductDTO add(ProductRequest productRequest) throws InvalidationException, NotFoundException, IOException;

    ProductDTO update(String id, BasicProductRequest productRequest) throws InvalidationException, NotFoundException, IOException;

    PageDTO<ProductDTO> getAll(Integer page, Integer size, String search, String category, List<String> sortColumns) throws NotFoundException, JsonProcessingException;

    ProductDTO get(String id) throws NotFoundException;

    void delete(String id) throws NotFoundException;
}
