package com.example.productservice.service;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDTO add(String productRequest, List<MultipartFile> files) throws InvalidException, NotFoundException;

    ProductDTO update(String id, String productRequest, List<MultipartFile> files) throws InvalidException, NotFoundException;

    PageResponse<ProductDTO> getAll(Integer page, Integer size, String shop, String search, String category) throws NotFoundException;

    ProductDTO get(String id) throws NotFoundException;

    void delete(String id) throws NotFoundException;
}
