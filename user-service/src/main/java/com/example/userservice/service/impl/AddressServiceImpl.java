package com.example.userservice.service.impl;

import com.example.servicefoundation.exception.I18nException;
import com.example.userservice.constant.I18nMessage;
import com.example.userservice.dto.AddressDto;
import com.example.userservice.dto.request.AddressRequest;
import com.example.userservice.entity.Address;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.AddressMapper;
import com.example.userservice.repository.AddressRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.util.SecurityUtils;
import com.example.userservice.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public AddressDto update(AddressRequest addressRequest) throws I18nException {
        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty()) {
            throw I18nException.builder()
                    .code(HttpStatus.UNAUTHORIZED)
                    .message(I18nMessage.ERROR_USER_UNKNOWN)
                    .build();
        }

        User user = userRepository.findById(userId.get())
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        Address address = new Address();
        if (user.getAddress() != null) address = user.getAddress();
        else user.setAddress(address);

        address.setCity(address.getCity());
        address.setDistrict(addressRequest.getDistrict());
        address.setWard(addressRequest.getWard());
        address.setCountry(addressRequest.getCountry());
        address.setDetail(addressRequest.getDetail());

        userRepository.save(user);
        return addressMapper.toDto(user.getAddress());
    }

    @Override
    public AddressDto get() throws I18nException {
        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty()) {
            throw I18nException.builder()
                    .code(HttpStatus.UNAUTHORIZED)
                    .message(I18nMessage.ERROR_USER_UNKNOWN)
                    .build();
        }

        User user = userRepository.findById(userId.get())
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        return addressMapper.toDto(user.getAddress());
    }
}
