package com.example.productservice.service;

import com.example.productservice.exception.InvalidException;
import com.example.productservice.payload.UpdateDetailPriceReq;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OrderMessagingService {
    void updateUnitPrice(UpdateDetailPriceReq request) throws JsonProcessingException;

    void createCustomer(String customerRequest) throws JsonProcessingException, InvalidException;
}
