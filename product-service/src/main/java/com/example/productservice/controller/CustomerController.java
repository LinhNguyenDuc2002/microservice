package com.example.productservice.controller;

import com.example.productservice.constant.ParameterConstant;
import com.example.productservice.constant.ResponseMessage;
import com.example.productservice.dto.CustomerDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.CustomerRequest;
import com.example.productservice.payload.response.CommonResponse;
import com.example.productservice.payload.response.PageResponse;
import com.example.productservice.service.CustomerService;
import com.example.productservice.util.ResponseUtil;
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
    public ResponseEntity<PageResponse<CustomerDTO>> getAll(
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
