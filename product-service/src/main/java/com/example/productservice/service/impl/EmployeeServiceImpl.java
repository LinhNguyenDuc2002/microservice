package com.example.productservice.service.impl;

import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.dto.EmployeeDTO;
import com.example.productservice.entity.Employee;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.mapper.EmployeeMapper;
import com.example.productservice.payload.CustomerRequest;
import com.example.productservice.repository.EmployeeRepository;
import com.example.productservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public EmployeeDTO create(CustomerRequest customerRequest) throws InvalidException {
        if (customerRequest == null ||
            !StringUtils.hasText(customerRequest.getAccountId()) ||
            !StringUtils.hasText(customerRequest.getFullname()) ||
            !StringUtils.hasText(customerRequest.getEmail()) ||
            !StringUtils.hasText(customerRequest.getPhone())) {
            throw new InvalidException(ExceptionMessage.ERROR_CUSTOMER_INVALID_INPUT);
        }

        if (!customerRequest.getRole().equals("EMPLOYEE")) {
            throw new InvalidException(ExceptionMessage.ERROR_CUSTOMER_INVALID_INPUT);
        }

        Employee employee = Employee.builder()
                .accountId(customerRequest.getAccountId())
                .fullname(customerRequest.getFullname())
                .phone(customerRequest.getPhone())
                .email(customerRequest.getEmail())
                .build();

        return employeeMapper.toDto(employeeRepository.save(employee));
    }
}
