package com.example.orderservice.controller;

import com.example.orderservice.constant.ParameterConstant;
import com.example.orderservice.dto.CheckingDetailDTO;
import com.example.orderservice.dto.DetailDTO;
import com.example.orderservice.dto.ShopDetailDTO;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.payload.response.CommonResponse;
import com.example.orderservice.payload.response.PageResponse;
import com.example.orderservice.service.DetailService;
import com.example.orderservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
            @RequestParam(name = "quantity", defaultValue = "1") Integer quantity) throws Exception {
        return ResponseUtil.wrapResponse(detailService.create(product, customer, quantity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponse<DetailDTO>> update(
            @PathVariable String id,
            @RequestParam(name = "quantity", required = false) Integer quantity) throws Exception {
        return ResponseUtil.wrapResponse(detailService.update(id, quantity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ShopDetailDTO>> getAll(
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "customer", required = false) String customerId,
            @RequestParam(name = "status", required = false) Boolean status) throws Exception {
        return ResponseEntity.ok(detailService.getAll(page, size, customerId, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<DetailDTO>> get(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(detailService.get(id));
    }

    @GetMapping("/{id}/exist")
    public ResponseEntity<CheckingDetailDTO> checkDetailExist(@PathVariable String id) throws NotFoundException {
        return ResponseEntity.ok(detailService.checkDetailExist(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable String id) throws NotFoundException, InvalidException {
        detailService.delete(id);
        return ResponseUtil.wrapResponse(null);
    }
}
