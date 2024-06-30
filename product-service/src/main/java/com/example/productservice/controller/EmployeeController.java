package com.example.productservice.controller;

import com.example.productservice.constant.ResponseMessage;
import com.example.productservice.dto.EmployeeDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.payload.CustomerRequest;
import com.example.productservice.payload.response.CommonResponse;
import com.example.productservice.service.EmployeeService;
import com.example.productservice.util.ResponseUtil;
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
