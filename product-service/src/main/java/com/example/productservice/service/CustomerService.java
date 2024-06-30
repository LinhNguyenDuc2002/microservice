package com.example.productservice.service;

import com.example.productservice.dto.CustomerDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.CustomerRequest;
import com.example.productservice.payload.response.PageResponse;

public interface CustomerService {
    CustomerDTO create(CustomerRequest customerRequest) throws InvalidException;

    PageResponse<CustomerDTO> getAll(Integer page, Integer size);

    CustomerDTO get(String id) throws NotFoundException;

    void delete(String id) throws NotFoundException;
}
