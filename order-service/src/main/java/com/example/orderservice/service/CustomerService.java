package com.example.orderservice.service;

import com.example.orderservice.dto.CustomerDTO;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.payload.CustomerRequest;
import com.example.orderservice.dto.PageDTO;

public interface CustomerService {
    CustomerDTO create(CustomerRequest customerRequest) throws InvalidException;

    PageDTO<CustomerDTO> getAll(Integer page, Integer size);

    CustomerDTO get(String id) throws NotFoundException;

    void delete(String id) throws NotFoundException;
}
