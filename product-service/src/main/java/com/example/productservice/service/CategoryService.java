package com.example.productservice.service;

import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.exception.InvalidationException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.dto.request.CategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryDTO add(CategoryRequest categoryRequest) throws InvalidationException;

    CategoryDTO update(String id, CategoryRequest categoryRequest) throws InvalidationException, NotFoundException;

    PageDTO<CategoryDTO> getAll(Integer page, Integer size, String search, List<String> sortColumns);

    List<CategoryDTO> getAll(String search);

    CategoryDTO get(String id) throws NotFoundException;

    void delete(String id) throws NotFoundException;
}
