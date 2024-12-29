package com.example.orderservice.service;

import com.example.orderservice.dto.ReceiverDto;
import com.example.orderservice.dto.request.ReceiverRequest;
import com.example.orderservice.exception.InvalidationException;
import com.example.orderservice.exception.NotFoundException;

import java.util.List;

public interface ReceiverService {
    ReceiverDto add(ReceiverRequest receiverRequest) throws InvalidationException;

    ReceiverDto update(String id, ReceiverRequest receiverRequest) throws NotFoundException, InvalidationException;

    ReceiverDto get(String id) throws NotFoundException;

    List<ReceiverDto> getAll(String id);

    void delete(String id) throws NotFoundException;
}
