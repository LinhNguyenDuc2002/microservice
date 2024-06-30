package com.example.userservice.service.impl;

import com.example.userservice.constant.ExceptionMessage;
import com.example.userservice.constant.ResponseMessage;
import com.example.userservice.dto.AddressDto;
import com.example.userservice.dto.request.AddressRequest;
import com.example.userservice.entity.Address;
import com.example.userservice.entity.User;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.mapper.AddressMapper;
import com.example.userservice.repository.AddressRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public AddressDto update(AddressRequest addressRequest, String id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    return NotFoundException.builder()
                            .message(ExceptionMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        if (user.getAddress() == null) {
            Address address = convertToAddress(addressRequest);
            user.setAddress(address);
            userRepository.save(user);
        } else {
            Address address = user.getAddress();

            if (StringUtils.hasText(addressRequest.getCity())) {
                address.setCity(address.getCity());
            }
            if (StringUtils.hasText(addressRequest.getDistrict())) {
                address.setDistrict(addressRequest.getDistrict());
            }
            if (StringUtils.hasText(addressRequest.getWard())) {
                address.setWard(addressRequest.getWard());
            }
            if (StringUtils.hasText(addressRequest.getCountry())) {
                address.setCountry(addressRequest.getCountry());
            }
            if (StringUtils.hasText(addressRequest.getSpecificAddress())) {
                address.setDetail(addressRequest.getSpecificAddress());
            }
            addressRepository.save(address);
        }

        return addressMapper.toDto(user.getAddress());
    }

    @Override
    public AddressDto get(String id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    return NotFoundException.builder()
                            .message(ExceptionMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        return addressMapper.toDto(user.getAddress());
    }

    private Address convertToAddress(AddressRequest addressRequest) {
        if (addressRequest == null) {
            return new Address();
        }

        return Address.builder()
                .city(addressRequest.getCity())
                .country(addressRequest.getCountry())
                .district(addressRequest.getDistrict())
                .ward(addressRequest.getWard())
                .detail(addressRequest.getSpecificAddress())
                .build();
    }
}
