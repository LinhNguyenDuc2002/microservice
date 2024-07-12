package com.example.orderservice.service;

import com.example.orderservice.dto.CustomerDTO;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.payload.CustomerRequest;
import com.example.orderservice.payload.response.PageResponse;

public interface CustomerService {
    CustomerDTO create(CustomerRequest customerRequest) throws InvalidException;

    PageResponse<CustomerDTO> getAll(Integer page, Integer size);

    CustomerDTO get(String id) throws NotFoundException;

    void delete(String id) throws NotFoundException;
}
