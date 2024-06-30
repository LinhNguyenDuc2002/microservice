package com.example.productservice.service.impl;

import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.dto.CustomerDTO;
import com.example.productservice.entity.Customer;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.CustomerMapper;
import com.example.productservice.mapper.ShopMapper;
import com.example.productservice.payload.CustomerRequest;
import com.example.productservice.payload.response.PageResponse;
import com.example.productservice.repository.CustomerRepository;
import com.example.productservice.repository.ShopRepository;
import com.example.productservice.service.CustomerService;
import com.example.productservice.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public CustomerDTO create(CustomerRequest customerRequest) throws InvalidException {
        if (customerRequest == null ||
                !StringUtils.hasText(customerRequest.getAccountId()) ||
                !StringUtils.hasText(customerRequest.getFullname()) ||
                !StringUtils.hasText(customerRequest.getEmail()) ||
                !StringUtils.hasText(customerRequest.getPhone())) {
            throw new InvalidException(ExceptionMessage.ERROR_CUSTOMER_INVALID_INPUT);
        }

        if (!customerRequest.getRole().equals("CUSTOMER")) {
            throw new InvalidException(ExceptionMessage.ERROR_CUSTOMER_INVALID_INPUT);
        }

        Customer customer = Customer.builder()
                .accountId(customerRequest.getAccountId())
                .fullname(customerRequest.getFullname())
                .phone(customerRequest.getPhone())
                .email(customerRequest.getEmail())
                .build();

        return customerMapper.toDto(customerRepository.save(customer));
    }

    @Override
    public PageResponse<CustomerDTO> getAll(Integer page, Integer size) {
        Pageable pageable = PageUtil.getPage(page, size);

        Page<Customer> customers = customerRepository.findAll(pageable);
        return PageResponse.<CustomerDTO>builder()
                .index(page)
                .totalPage(customers.getTotalPages())
                .elements(customerMapper.toDtoList(customers.getContent()))
                .build();
    }

    @Override
    public CustomerDTO get(String id) throws NotFoundException {
        Optional<Customer> check = customerRepository.findById(id);

        if (!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_CUSTOMER_NOT_FOUND);
        }

        return customerMapper.toDto(check.get());
    }

    @Override
    public void delete(String id) throws NotFoundException {
        Optional<Customer> check = customerRepository.findById(id);

        if (!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_CUSTOMER_NOT_FOUND);
        }
        customerRepository.deleteById(id);
    }
}
