package com.example.productservice.service;

import com.example.productservice.dto.ProductTypeDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.ProductTypeRequest;

import java.util.List;

public interface ProductTypeService {
    ProductTypeDTO add(String id, ProductTypeRequest productTypeRequest) throws NotFoundException, InvalidException;

    List<ProductTypeDTO> getAll(String id) throws NotFoundException, InvalidException;

    ProductTypeDTO get(String id) throws NotFoundException, InvalidException;

    ProductTypeDTO update(String id, ProductTypeRequest productTypeRequest) throws NotFoundException, InvalidException;

    void delete(String id) throws NotFoundException, InvalidException;
}
