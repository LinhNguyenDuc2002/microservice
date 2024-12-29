package com.example.productservice.mapper;

import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.dto.CommentDTO;
import com.example.productservice.entity.Category;
import com.example.productservice.entity.Comment;
import com.example.productservice.entity.Image;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryMapper extends AbstractMapper<Category, CategoryDTO> {
    @Override
    public Class<CategoryDTO> getDtoClass() {
        return CategoryDTO.class;
    }

    @Override
    public Class<Category> getEntityClass() {
        return Category.class;
    }
}
