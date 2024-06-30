package com.example.productservice.service;

import com.example.productservice.dto.BillDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.BillRequest;
import com.example.productservice.payload.UpdateBillRequest;
import com.example.productservice.payload.response.PageResponse;

import java.util.Date;

public interface BillService {
    BillDTO create(BillRequest billRequest) throws InvalidException;

    BillDTO update(String id, UpdateBillRequest updateBillRequest) throws InvalidException, NotFoundException;

    PageResponse<BillDTO> getAll(Integer page, Integer size, Date startAt, Date endAt);

    BillDTO get(String id) throws NotFoundException;

    PageResponse<BillDTO> getByCustomerId(Integer page, Integer size, String id) throws NotFoundException;

    BillDTO changeStatus(String id, String status) throws NotFoundException, InvalidException;

    void delete(String id) throws NotFoundException;
}
