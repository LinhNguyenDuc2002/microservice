package com.example.productservice.controller;

import com.example.productservice.dto.ProductTypeDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.ProductTypeRequest;
import com.example.productservice.payload.response.CommonResponse;
import com.example.productservice.service.ProductTypeService;
import com.example.productservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/product-type")
public class ProductTypeController {
    @Autowired
    private ProductTypeService productTypeService;

    @PostMapping("/product/{id}")
    public ResponseEntity<CommonResponse<ProductTypeDTO>> add(
            @PathVariable String id,
            @RequestBody ProductTypeRequest productTypeRequest) throws NotFoundException, InvalidException {
        return ResponseUtil.wrapResponse(productTypeService.add(id, productTypeRequest));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<CommonResponse<List<ProductTypeDTO>>> getAll(@PathVariable String id) throws NotFoundException, InvalidException {
        return ResponseUtil.wrapResponse(productTypeService.getAll(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ProductTypeDTO>> get(@PathVariable String id) throws NotFoundException, InvalidException {
        return ResponseUtil.wrapResponse(productTypeService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<ProductTypeDTO>> update(
            @PathVariable String id,
            @RequestBody ProductTypeRequest productTypeRequest) throws NotFoundException, InvalidException {
        return ResponseUtil.wrapResponse(productTypeService.update(id, productTypeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable String id) throws NotFoundException, InvalidException {
        productTypeService.delete(id);
        return ResponseUtil.wrapResponse(null);
    }
}
