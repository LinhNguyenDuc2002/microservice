package com.example.productservice.service;

import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.CategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryDTO add(CategoryRequest categoryRequest) throws InvalidException;

    CategoryDTO update(String id, CategoryRequest categoryRequest) throws InvalidException, NotFoundException;

    List<CategoryDTO> getAll();

    void delete(String id) throws NotFoundException;
}
