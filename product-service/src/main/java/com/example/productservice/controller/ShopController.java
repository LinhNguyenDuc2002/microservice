package com.example.productservice.controller;

import com.example.productservice.dto.ShopDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.ShopRequest;
import com.example.productservice.payload.response.CommonResponse;
import com.example.productservice.service.ShopService;
import com.example.productservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    @PostMapping("/user/{id}")
    public ResponseEntity<CommonResponse<ShopDTO>> create(
            @PathVariable String id,
            @RequestBody ShopRequest shopRequest) throws InvalidException, NotFoundException {
        return ResponseUtil.wrapResponse(shopService.create(id, shopRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ShopDTO>> create(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(shopService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<ShopDTO>> update(
            @PathVariable String id,
            @RequestBody ShopRequest shopRequest) throws NotFoundException {
        return ResponseUtil.wrapResponse(shopService.update(id, shopRequest));
    }
}
