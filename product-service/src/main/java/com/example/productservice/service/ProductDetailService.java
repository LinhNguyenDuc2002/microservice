package com.example.productservice.service;

import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.ProductDetailDTO;
import com.example.productservice.dto.request.ProductDetailRequest;
import com.example.productservice.exception.InvalidationException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.dto.ProductDetailCheckingDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public interface ProductDetailService {
    ProductDetailDTO add(String id, ProductDetailRequest productDetailRequest) throws NotFoundException, InvalidationException, IOException;

    PageDTO<ProductDetailDTO> getAll(String id, Integer page, Integer size, String search, List<String> sortColumns) throws NotFoundException, InvalidationException;

    ProductDetailDTO get(String id) throws NotFoundException, InvalidationException;

    ProductDetailDTO update(String id, ProductDetailRequest productDetailRequest) throws NotFoundException, InvalidationException, IOException;

    void delete(String id) throws NotFoundException, InvalidationException;

    ProductDetailCheckingDto checkExist(String id, Integer quantity);
}
