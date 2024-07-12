package com.example.orderservice.service;

import com.example.orderservice.dto.EmployeeDTO;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.payload.CustomerRequest;

public interface EmployeeService {
    EmployeeDTO create(CustomerRequest customerRequest) throws InvalidException;
}
