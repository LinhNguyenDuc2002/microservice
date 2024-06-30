package com.example.productservice.service;

import com.example.productservice.dto.DetailDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.response.PageResponse;

public interface DetailService {
    DetailDTO create(String product, String customer, Integer quantity) throws NotFoundException, InvalidException;

    DetailDTO update(String id, Integer quantity) throws NotFoundException, InvalidException;

    PageResponse<DetailDTO> getAll(Integer page, Integer size, Boolean status);

    DetailDTO get(String id) throws NotFoundException;

    void delete(String id) throws NotFoundException;
}
