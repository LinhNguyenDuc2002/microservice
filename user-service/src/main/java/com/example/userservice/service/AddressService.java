package com.example.userservice.service;

import com.example.userservice.dto.AddressDto;
import com.example.userservice.dto.request.AddressRequest;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.ValidationException;

public interface AddressService {
    AddressDto update(AddressRequest addressRequest, String id) throws ValidationException, NotFoundException;

    AddressDto get(String id) throws NotFoundException;
}
