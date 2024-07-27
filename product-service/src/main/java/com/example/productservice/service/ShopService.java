package com.example.productservice.service;

import com.example.productservice.dto.ShopDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.ShopRequest;

public interface ShopService {
    ShopDTO getMyShop() throws InvalidException, NotFoundException;

    ShopDTO create(String id, ShopRequest shopRequest) throws Exception;

    ShopDTO get(String id) throws NotFoundException;

    ShopDTO update(String id, ShopRequest shopRequest) throws NotFoundException;

    void delete(String id) throws Exception;
}
