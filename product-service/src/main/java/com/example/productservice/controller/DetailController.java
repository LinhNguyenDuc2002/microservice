package com.example.productservice.controller;

import com.example.productservice.constant.ParameterConstant;
import com.example.productservice.dto.DetailDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.response.CommonResponse;
import com.example.productservice.payload.response.PageResponse;
import com.example.productservice.service.DetailService;
import com.example.productservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/detail")
public class DetailController {
    @Autowired
    private DetailService detailService;

    @PostMapping
    public ResponseEntity<CommonResponse<DetailDTO>> create(
            @RequestParam(name = "product", required = true) String product,
            @RequestParam(name = "customer", required = true) String customer,
            @RequestParam(name = "quantity", defaultValue = "1") Integer quantity) throws NotFoundException, InvalidException {
        return ResponseUtil.wrapResponse(detailService.create(product, customer, quantity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponse<DetailDTO>> update(
            @PathVariable String id,
            @RequestParam(name = "quantity", required = false) Integer quantity) throws NotFoundException, InvalidException {
        return ResponseUtil.wrapResponse(detailService.update(id, quantity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<DetailDTO>> getAll(
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "status") Boolean status) {
        return ResponseEntity.ok(detailService.getAll(page, size, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<DetailDTO>> get(String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(detailService.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable String id) throws NotFoundException, InvalidException {
        detailService.delete(id);
        return ResponseUtil.wrapResponse(null);
    }
}
