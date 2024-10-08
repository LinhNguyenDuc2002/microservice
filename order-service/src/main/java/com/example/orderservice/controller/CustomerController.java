package com.example.orderservice.controller;

import com.example.orderservice.constant.ParameterConstant;
import com.example.orderservice.constant.ResponseMessage;
import com.example.orderservice.dto.CustomerDTO;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.payload.CustomerRequest;
import com.example.orderservice.payload.response.CommonResponse;
import com.example.orderservice.dto.PageDTO;
import com.example.orderservice.service.CustomerService;
import com.example.orderservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CommonResponse<CustomerDTO>> create(@RequestBody CustomerRequest customerRequest) throws InvalidException {
        return ResponseUtil.wrapResponse(customerService.create(customerRequest), ResponseMessage.CREATE_CUSTOMER_SUCCESS);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<PageDTO<CustomerDTO>> getAll(
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size) {
        return ResponseEntity.ok(customerService.getAll(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<CommonResponse<CustomerDTO>> get(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(customerService.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable String id) throws NotFoundException {
        customerService.delete(id);
        return ResponseUtil.wrapResponse(null);
    }
}
