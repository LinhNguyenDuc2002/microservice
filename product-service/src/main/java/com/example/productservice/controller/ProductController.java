package com.example.productservice.controller;

import com.example.productservice.constant.ParameterConstant;
import com.example.productservice.dto.ExistingProductCheckDTO;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.response.CommonResponse;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.service.ProductService;
import com.example.productservice.util.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('SELLER')")
    public ResponseEntity<CommonResponse<ProductDTO>> add(
            @RequestParam(name = "images", required = false) List<MultipartFile> files,
            @RequestParam(name = "product", required = false) String productRequest) throws InvalidException, NotFoundException {
        return ResponseUtil.wrapResponse(productService.add(productRequest, files));
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('SELLER')")
    public ResponseEntity<CommonResponse<ProductDTO>> update(
            @PathVariable String id,
            @RequestParam("images") List<MultipartFile> files,
            @RequestParam("product") String productRequest) throws InvalidException, NotFoundException {
        return ResponseUtil.wrapResponse(productService.update(id, productRequest, files));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<PageDTO<ProductDTO>>> getAll(
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "shop", required = false) String shop,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "sort-columns", required = false) List<String> sortColumns) throws NotFoundException, JsonProcessingException {
        return ResponseUtil.wrapResponse(productService.getAll(page, size, shop, search, category, sortColumns));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ProductDTO>> get(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(productService.get(id));
    }

    @GetMapping("/{id}/exist")
    public ResponseEntity<ExistingProductCheckDTO> checkProductExist(
            @PathVariable String id,
            @RequestParam("type") String productTypeId,
            @RequestParam("quantity") Integer quantity) throws NotFoundException, InvalidException {
        return ResponseEntity.ok(productService.checkProductExist(id, productTypeId, quantity));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable String id) throws NotFoundException {
        productService.delete(id);
        return ResponseUtil.wrapResponse(null);
    }
}
