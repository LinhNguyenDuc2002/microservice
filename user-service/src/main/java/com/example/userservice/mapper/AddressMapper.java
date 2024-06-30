package com.example.userservice.mapper;

import com.example.userservice.dto.AddressDto;
import com.example.userservice.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper extends AbstractMapper<Address, AddressDto> {
    @Override
    public Class<AddressDto> getDtoClass() {
        return AddressDto.class;
    }

    @Override
    public Class<Address> getEntityClass() {
        return Address.class;
    }
}
