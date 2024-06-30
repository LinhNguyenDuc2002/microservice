package com.example.userservice.controller;

import com.example.userservice.constant.ResponseMessage;
import com.example.userservice.dto.AddressDto;
import com.example.userservice.dto.request.AddressRequest;
import com.example.userservice.dto.response.CommonResponse;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.ValidationException;
import com.example.userservice.service.AddressService;
import com.example.userservice.util.HandleBindingResult;
import com.example.userservice.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
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

    @GetMapping("/user/{id}")
    public ResponseEntity<CommonResponse<AddressDto>> get(@PathVariable String id) throws NotFoundException, ValidationException {
        return ResponseUtil.wrapResponse(addressService.get(id), ResponseMessage.GET_ADDRESS_SUCCESS.getMessage());
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<CommonResponse<AddressDto>> update(
            @PathVariable String id,
            @Valid @RequestBody AddressRequest addressRequest,
            BindingResult bindingResult) throws NotFoundException, ValidationException {
        HandleBindingResult.handle(bindingResult, addressRequest);
        return ResponseUtil.wrapResponse(addressService.update(addressRequest, id), ResponseMessage.UPDATE_ADDRESS_SUCCESS.getMessage());
    }
}
