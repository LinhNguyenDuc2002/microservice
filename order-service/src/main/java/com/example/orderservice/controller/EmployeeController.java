package com.example.orderservice.controller;

import com.example.orderservice.constant.ResponseMessage;
import com.example.orderservice.dto.EmployeeDTO;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.payload.CustomerRequest;
import com.example.orderservice.payload.response.CommonResponse;
import com.example.orderservice.service.EmployeeService;
import com.example.orderservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonResponse<EmployeeDTO>> create(@RequestBody CustomerRequest customerRequest) throws InvalidException {
        return ResponseUtil.wrapResponse(employeeService.create(customerRequest), ResponseMessage.CREATE_EMPLOYEE_SUCCESS);
    }
}
