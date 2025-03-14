package com.example.productservice.service;

import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.request.CategoryRequest;
import com.example.servicefoundation.exception.I18nException;

import java.util.List;

public interface CategoryService {
    CategoryDTO add(CategoryRequest categoryRequest);

    CategoryDTO update(String id, CategoryRequest categoryRequest) throws I18nException;

    PageDTO<CategoryDTO> getAll(Integer page, Integer size, String search, List<String> sortColumns);

    List<CategoryDTO> getAll(String search);

    CategoryDTO get(String id) throws I18nException;

    void delete(String id) throws I18nException;
}
