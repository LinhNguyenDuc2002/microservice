package com.example.productservice.service.impl;

import com.example.productservice.constant.I18nMessage;
import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.request.CategoryRequest;
import com.example.productservice.entity.Category;
import com.example.productservice.mapper.CategoryMapper;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.repository.predicate.CategoryPredicate;
import com.example.productservice.service.CategoryService;
import com.example.servicefoundation.exception.I18nException;
import com.example.servicefoundation.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryDTO add(CategoryRequest categoryRequest) {
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .status(true)
                .build();
        categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDTO update(String id, CategoryRequest categoryRequest) throws I18nException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_CATEGORY_NOT_FOUND)
                            .build();
                });

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @Override
    public PageDTO<CategoryDTO> getAll(Integer page, Integer size, String search, List<String> sortColumns) {
        Pageable pageable = (sortColumns == null) ? PaginationUtil.getPage(page, size) : PaginationUtil.getPage(page, size, sortColumns.toArray(new String[0]));

        CategoryPredicate categoryPredicate = new CategoryPredicate().search(search);
        Page<Category> categories = categoryRepository.findAll(categoryPredicate.getCriteria(), pageable);

        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        categories.getContent().stream().forEach(category -> {
            CategoryDTO categoryDTO = categoryMapper.toDto(category);
            categoryDTO.setTotal(category.getProducts().size());
            categoryDTOS.add(categoryDTO);
        });

        PageDTO pageDTO = PageDTO.<CategoryDTO>builder()
                .index(categories.getNumber())
                .totalPage(categories.getTotalPages())
                .elements(categoryDTOS)
                .build();

        return pageDTO;
    }

    @Override
    public List<CategoryDTO> getAll(String search) {
        CategoryPredicate categoryPredicate = new CategoryPredicate().search(search);
        List<Category> categories = categoryRepository.findAll(categoryPredicate.getCriteria());
        return categoryMapper.toDtoList(categories);
    }

    @Override
    public CategoryDTO get(String id) throws I18nException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_CATEGORY_NOT_FOUND)
                            .build();
                });

        return categoryMapper.toDto(category);
    }

    @Override
    public void delete(String id) throws I18nException {
        Optional<Category> check = categoryRepository.findById(id);
        if (!check.isPresent()) {
            throw I18nException.builder()
                    .code(HttpStatus.NOT_FOUND)
                    .message(I18nMessage.ERROR_CATEGORY_NOT_FOUND)
                    .build();
        }

        categoryRepository.deleteById(id);
    }
}
