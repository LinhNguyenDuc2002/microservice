package com.example.orderservice.service;

import com.example.orderservice.dto.BillDTO;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.payload.BillRequest;
import com.example.orderservice.payload.UpdateBillRequest;
import com.example.orderservice.dto.PageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Date;
import java.util.List;

public interface BillService {
    List<BillDTO> create(BillRequest billRequest) throws Exception;

    BillDTO update(String id, UpdateBillRequest updateBillRequest) throws InvalidException, NotFoundException;

    PageDTO<BillDTO> getAll(String id, Integer page, Integer size, Date startAt, Date endAt, String status, List<String> sortColumns) throws Exception;

    BillDTO get(String id) throws NotFoundException;

    PageDTO<BillDTO> getByCustomerId(Integer page, Integer size, String status, String id, List<String> sortColumns) throws NotFoundException;

    BillDTO changeStatus(String id, String status) throws NotFoundException, InvalidException, JsonProcessingException;

    void delete(String id) throws NotFoundException;
}
