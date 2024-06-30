package com.example.productservice.controller;

import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.CategoryRequest;
import com.example.productservice.payload.response.CommonResponse;
import com.example.productservice.service.CategoryService;
import com.example.productservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonResponse<CategoryDTO>> add(@RequestBody CategoryRequest categoryRequest) throws InvalidException {
        return ResponseUtil.wrapResponse(categoryService.add(categoryRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonResponse<CategoryDTO>> update(@PathVariable String id, @RequestBody CategoryRequest categoryRequest) throws InvalidException, NotFoundException {
        return ResponseUtil.wrapResponse(categoryService.update(id, categoryRequest));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CategoryDTO>>> getAll() {
        return ResponseUtil.wrapResponse(categoryService.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable String id) throws NotFoundException {
        categoryService.delete(id);
        return ResponseUtil.wrapResponse(null);
    }
}
