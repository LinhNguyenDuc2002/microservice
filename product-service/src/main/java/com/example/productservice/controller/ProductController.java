package com.example.productservice.controller;

import com.example.productservice.constant.ParameterConstant;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.response.CommonResponse;
import com.example.productservice.payload.response.PageResponse;
import com.example.productservice.service.ProductService;
import com.example.productservice.util.ResponseUtil;
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

    @PostMapping(value = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonResponse<ProductDTO>> add(
            @RequestParam("images") List<MultipartFile> files,
            @RequestParam("product") String productRequest) throws InvalidException, NotFoundException {
        return ResponseUtil.wrapResponse(productService.add(productRequest, files));
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonResponse<ProductDTO>> update(
            @PathVariable String id,
            @RequestParam("images") List<MultipartFile> files,
            @RequestParam("product") String productRequest) throws InvalidException, NotFoundException {
        return ResponseUtil.wrapResponse(productService.update(id, productRequest, files));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductDTO>> getAll(
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "shop", required = false) String shop,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "category", required = false) String category) throws NotFoundException {
        return ResponseEntity.ok(productService.getAll(page, size, shop, search, category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ProductDTO>> get(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(productService.get(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable String id) throws NotFoundException {
        productService.delete(id);
        return ResponseUtil.wrapResponse(null);
    }
}
