package com.example.productservice.service;

import com.example.productservice.dto.CustomerDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.CustomerRequest;
import com.example.productservice.dto.PageDTO;

public interface CustomerService {
    CustomerDTO create(CustomerRequest customerRequest) throws InvalidException;

    PageDTO<CustomerDTO> getAll(Integer page, Integer size);

    CustomerDTO get(String id) throws NotFoundException;

    void delete(String id) throws NotFoundException;
}
