package com.example.orderservice.service;

import com.example.orderservice.dto.ReceiverDto;
import com.example.orderservice.dto.request.ReceiverRequest;
import com.example.servicefoundation.exception.I18nException;

import java.util.List;

public interface ReceiverService {
    ReceiverDto add(ReceiverRequest receiverRequest) throws I18nException;

    ReceiverDto update(String id, ReceiverRequest receiverRequest) throws I18nException;

    ReceiverDto get(String id) throws I18nException;

    List<ReceiverDto> getAll(String id);

    void delete(String id) throws I18nException;
}
