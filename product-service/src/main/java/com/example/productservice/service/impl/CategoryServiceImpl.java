package com.example.productservice.service.impl;

import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.entity.Category;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.CategoryMapper;
import com.example.productservice.payload.CategoryRequest;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryDTO add(CategoryRequest categoryRequest) throws InvalidException {
        if (categoryRequest == null || StringUtils.hasText(categoryRequest.getName())) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        Category category = Category.builder()
                .name(categoryRequest.getName())
                .note(categoryRequest.getNote())
                .build();

        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDTO update(String id, CategoryRequest categoryRequest) throws InvalidException, NotFoundException {
        Optional<Category> check = categoryRepository.findById(id);

        if (!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_CATEGORY_NOT_FOUND);
        }

        if (categoryRequest == null || !StringUtils.hasText(categoryRequest.getName())) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        Category category = check.get();
        category.setName(categoryRequest.getName());
        category.setNote(categoryRequest.getNote());
        categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDTO> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }

    @Override
    public void delete(String id) throws NotFoundException {
        Optional<Category> check = categoryRepository.findById(id);

        if (!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_CATEGORY_NOT_FOUND);
        }

        categoryRepository.deleteById(id);
    }
}
