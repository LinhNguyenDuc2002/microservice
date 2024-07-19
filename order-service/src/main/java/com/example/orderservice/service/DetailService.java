package com.example.orderservice.service;

import com.example.orderservice.dto.CheckingDetailDTO;
import com.example.orderservice.dto.DetailDTO;
import com.example.orderservice.dto.ShopDetailDTO;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.payload.response.PageResponse;

public interface DetailService {
    DetailDTO create(String product, String customer, Integer quantity) throws Exception;

    DetailDTO update(String id, Integer quantity) throws Exception;

    PageResponse<ShopDetailDTO> getAll(Integer page, Integer size, String customerId, Boolean status);

    DetailDTO get(String id) throws NotFoundException;

    void delete(String id) throws NotFoundException;

    CheckingDetailDTO checkDetailExist(String id) throws NotFoundException, InvalidException;
}
