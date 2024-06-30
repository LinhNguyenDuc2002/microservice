package com.example.productservice.service;

import com.example.productservice.dto.ShopDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.ShopRequest;

public interface ShopService {
    ShopDTO create(String id, ShopRequest shopRequest) throws InvalidException, NotFoundException;

    ShopDTO get(String id) throws NotFoundException;

    ShopDTO update(String id, ShopRequest shopRequest) throws NotFoundException;
}
