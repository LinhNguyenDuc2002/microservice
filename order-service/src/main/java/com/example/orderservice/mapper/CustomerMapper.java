package com.example.orderservice.mapper;

import com.example.orderservice.dto.CustomerDTO;
import com.example.orderservice.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper extends AbstractMapper<Customer, CustomerDTO> {
    @Override
    public Class<CustomerDTO> getDtoClass() {
        return CustomerDTO.class;
    }

    @Override
    public Class<Customer> getEntityClass() {
        return Customer.class;
    }
}
