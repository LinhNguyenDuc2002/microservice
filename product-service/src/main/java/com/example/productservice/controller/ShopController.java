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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopController {
    @Autowired
    private ShopService shopService;

    @GetMapping("/my-shop")
    public ResponseEntity<CommonResponse<ShopDTO>> getMyShop() throws NotFoundException, InvalidException {
        return ResponseUtil.wrapResponse(shopService.getMyShop());
    }

    @PostMapping("/shop/customer/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<ShopDTO>> create(
            @PathVariable String id,
            @RequestBody ShopRequest shopRequest) throws Exception {
        return ResponseUtil.wrapResponse(shopService.create(id, shopRequest));
    }

    @GetMapping("/shop/{id}")
    public ResponseEntity<CommonResponse<ShopDTO>> get(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(shopService.get(id));
    }

    @PutMapping("/shop/{id}")
    @PreAuthorize("hasAnyRole('SELLER')")
    public ResponseEntity<CommonResponse<ShopDTO>> update(
            @PathVariable String id,
            @RequestBody ShopRequest shopRequest) throws NotFoundException {
        return ResponseUtil.wrapResponse(shopService.update(id, shopRequest));
    }

    @DeleteMapping("/shop/{id}")
    @PreAuthorize("hasAnyRole('SELLER')")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable String id) throws Exception {
        shopService.delete(id);
        return ResponseUtil.wrapResponse(null, "");
    }

    @GetMapping("/shop/{id}/exist")
    public ResponseEntity<Boolean> checkShopExist(@PathVariable String id) throws NotFoundException {
        return ResponseEntity.ok(shopService.checkShopExist(id));
    }
}
