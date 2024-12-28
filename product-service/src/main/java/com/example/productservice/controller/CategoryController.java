package com.example.productservice.controller;

import com.example.productservice.constant.I18nMessage;
import com.example.productservice.constant.ParameterConstant;
import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.request.CategoryRequest;
import com.example.productservice.dto.response.Response;
import com.example.productservice.exception.InvalidationException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.i18n.I18nService;
import com.example.productservice.service.CategoryService;
import com.example.productservice.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private I18nService i18nService;

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Response<CategoryDTO>> add(@Valid @RequestBody CategoryRequest categoryRequest) throws InvalidationException {
        return ResponseUtil.wrapResponse(
                categoryService.add(categoryRequest),
                i18nService.getMessage(I18nMessage.INFO_CREATE_CATEGORY, LocaleContextHolder.getLocale())
        );
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Response<CategoryDTO>> update(
            @PathVariable String id,
            @Valid @RequestBody CategoryRequest categoryRequest) throws InvalidationException, NotFoundException {
        return ResponseUtil.wrapResponse(
                categoryService.update(id, categoryRequest),
                i18nService.getMessage(I18nMessage.INFO_UPDATE_CATEGORY, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/pagination")
    public ResponseEntity<Response<PageDTO<CategoryDTO>>> getAll(
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "sort-columns", required = false) List<String> sortColumns) {
        return ResponseUtil.wrapResponse(
                categoryService.getAll(page, size, search, sortColumns),
                i18nService.getMessage(I18nMessage.INFO_GET_CATEGORY, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping
    public ResponseEntity<Response<List<CategoryDTO>>> getAll(@RequestParam(name = "search", required = false) String search) {
        return ResponseUtil.wrapResponse(
                categoryService.getAll(search),
                i18nService.getMessage(I18nMessage.INFO_GET_CATEGORY, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<CategoryDTO>> get(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(
                categoryService.get(id),
                i18nService.getMessage(I18nMessage.INFO_GET_CATEGORY, LocaleContextHolder.getLocale())
        );
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Response<Void>> delete(@PathVariable String id) throws NotFoundException {
        categoryService.delete(id);
        return ResponseUtil.wrapResponse(
                i18nService.getMessage(I18nMessage.INFO_DELETE_CATEGORY, LocaleContextHolder.getLocale())
        );
    }
}
