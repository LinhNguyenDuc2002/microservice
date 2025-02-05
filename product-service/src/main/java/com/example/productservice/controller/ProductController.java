package com.example.productservice.controller;

import com.example.productservice.constant.I18nMessage;
import com.example.productservice.constant.ParameterConstant;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.request.BasicProductRequest;
import com.example.productservice.dto.request.ProductRequest;
import com.example.productservice.service.ProductService;
import com.example.servicefoundation.base.response.Response;
import com.example.servicefoundation.exception.I18nException;
import com.example.servicefoundation.i18n.I18nService;
import com.example.servicefoundation.util.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private I18nService i18nService;

    @PostMapping
//    @PreAuthorize("hasAnyRole('SELLER')")
    public ResponseEntity<Response<ProductDTO>> add(@Valid @ModelAttribute ProductRequest productRequest) throws IOException, I18nException {
        return ResponseUtil.wrapResponse(
                productService.add(productRequest),
                i18nService.getMessage(I18nMessage.INFO_CREATE_PRODUCT, LocaleContextHolder.getLocale())
        );
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    @PreAuthorize("hasAnyRole('SELLER')")
    public ResponseEntity<Response<ProductDTO>> update(
            @PathVariable String id,
            @Valid @ModelAttribute BasicProductRequest productRequest) throws IOException, I18nException {
        return ResponseUtil.wrapResponse(
                productService.update(id, productRequest),
                i18nService.getMessage(I18nMessage.INFO_UPDATE_PRODUCT, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping
    public ResponseEntity<Response<PageDTO<ProductDTO>>> getAll(
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "sort-columns", required = false) List<String> sortColumns) throws JsonProcessingException {
        return ResponseUtil.wrapResponse(
                productService.getAll(page, size, search, category, sortColumns),
                i18nService.getMessage(I18nMessage.INFO_GET_ALL_PRODUCT, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ProductDTO>> get(@PathVariable String id) throws I18nException {
        return ResponseUtil.wrapResponse(
                productService.get(id),
                i18nService.getMessage(I18nMessage.INFO_GET_PRODUCT, LocaleContextHolder.getLocale())
        );
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<Response<Void>> delete(@PathVariable String id) throws I18nException {
        productService.delete(id);
        return ResponseUtil.wrapResponse(
                i18nService.getMessage(I18nMessage.INFO_DELETE_PRODUCT, LocaleContextHolder.getLocale())
        );
    }
}
