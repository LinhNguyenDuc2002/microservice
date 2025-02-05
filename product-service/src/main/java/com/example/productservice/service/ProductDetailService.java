package com.example.productservice.service;

import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.ProductDetailCheckingDto;
import com.example.productservice.dto.ProductDetailDTO;
import com.example.productservice.dto.request.ProductDetailRequest;
import com.example.servicefoundation.exception.I18nException;

import java.io.IOException;
import java.util.List;

public interface ProductDetailService {
    ProductDetailDTO add(String id, ProductDetailRequest productDetailRequest) throws IOException, I18nException;

    PageDTO<ProductDetailDTO> getAll(String id, Integer page, Integer size, String search, List<String> sortColumns) throws I18nException;

    ProductDetailDTO get(String id) throws I18nException;

    ProductDetailDTO update(String id, ProductDetailRequest productDetailRequest) throws IOException, I18nException;

    void delete(String id) throws I18nException;

    ProductDetailCheckingDto checkExist(String id, Integer quantity);
}
