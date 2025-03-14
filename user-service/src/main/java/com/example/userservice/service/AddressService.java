package com.example.userservice.service;

import com.example.servicefoundation.exception.I18nException;
import com.example.userservice.dto.AddressDto;
import com.example.userservice.dto.request.AddressRequest;

public interface AddressService {
    AddressDto update(AddressRequest addressRequest, String id) throws I18nException;

    AddressDto get(String id) throws I18nException;
}
