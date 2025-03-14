package com.example.userservice.controller;

import com.example.servicefoundation.base.response.Response;
import com.example.servicefoundation.exception.I18nException;
import com.example.servicefoundation.i18n.I18nService;
import com.example.servicefoundation.util.ResponseUtil;
import com.example.userservice.constant.I18nMessage;
import com.example.userservice.dto.AddressDto;
import com.example.userservice.dto.request.AddressRequest;
import com.example.userservice.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private I18nService i18nService;

    @GetMapping("/user/{id}")
    public ResponseEntity<Response<AddressDto>> get(@PathVariable String id) throws I18nException {
        return ResponseUtil.wrapResponse(
                addressService.get(id),
                i18nService.getMessage(I18nMessage.INFO_GET_ADDRESS, LocaleContextHolder.getLocale())
        );
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Response<AddressDto>> update(@PathVariable String id, @Valid @RequestBody AddressRequest addressRequest) throws I18nException {
        return ResponseUtil.wrapResponse(
                addressService.update(addressRequest, id),
                i18nService.getMessage(I18nMessage.INFO_UPDATE_ADDRESS, LocaleContextHolder.getLocale())
        );
    }
}
