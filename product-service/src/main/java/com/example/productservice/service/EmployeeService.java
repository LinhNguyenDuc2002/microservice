package com.example.productservice.service;

import com.example.productservice.dto.EmployeeDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.payload.CustomerRequest;

public interface EmployeeService {
    EmployeeDTO create(CustomerRequest customerRequest) throws InvalidException;
}
