package com.example.productservice.controller;

import com.example.productservice.constant.I18nMessage;
import com.example.productservice.constant.ParameterConstant;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.ProductDetailCheckingDto;
import com.example.productservice.dto.ProductDetailDTO;
import com.example.productservice.dto.request.ProductDetailRequest;
import com.example.productservice.service.ProductDetailService;
import com.example.servicefoundation.base.response.Response;
import com.example.servicefoundation.exception.I18nException;
import com.example.servicefoundation.i18n.I18nService;
import com.example.servicefoundation.util.ResponseUtil;
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

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product-detail")
public class ProductDetailController {
    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private I18nService i18nService;

    @PostMapping("/product/{id}")
    public ResponseEntity<Response<ProductDetailDTO>> add(
            @PathVariable String id,
            @Valid @RequestBody ProductDetailRequest productDetailRequest) throws IOException, I18nException {
        return ResponseUtil.wrapResponse(
                productDetailService.add(id, productDetailRequest),
                i18nService.getMessage(I18nMessage.INFO_ADD_PRODUCT_DETAIL, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Response<PageDTO<ProductDetailDTO>>> getAll(
            @PathVariable String id,
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "sort-columns", required = false) List<String> sortColumns) throws I18nException {
        return ResponseUtil.wrapResponse(
                productDetailService.getAll(id, page, size, search, sortColumns),
                i18nService.getMessage(I18nMessage.INFO_GET_ALL_PRODUCT_DETAIL, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ProductDetailDTO>> get(@PathVariable String id) throws I18nException {
        return ResponseUtil.wrapResponse(
                productDetailService.get(id),
                i18nService.getMessage(I18nMessage.INFO_GET_PRODUCT_DETAIL, LocaleContextHolder.getLocale())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<ProductDetailDTO>> update(
            @PathVariable String id,
            @Valid @RequestBody ProductDetailRequest productDetailRequest) throws IOException, I18nException {
        return ResponseUtil.wrapResponse(
                productDetailService.update(id, productDetailRequest),
                i18nService.getMessage(I18nMessage.INFO_UPDATE_PRODUCT_DETAIL, LocaleContextHolder.getLocale())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable String id) throws I18nException {
        productDetailService.delete(id);
        return ResponseUtil.wrapResponse(
                i18nService.getMessage(I18nMessage.INFO_DELETE_PRODUCT_DETAIL, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/{id}/exist")
    public ResponseEntity<ProductDetailCheckingDto> checkExist(
            @PathVariable String id,
            @RequestParam(name = "quantity", defaultValue = "1") Integer quantity) {
        return ResponseEntity.ok(productDetailService.checkExist(id, quantity));
    }
}
