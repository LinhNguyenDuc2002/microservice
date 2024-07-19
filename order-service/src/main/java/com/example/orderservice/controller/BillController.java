package com.example.orderservice.controller;

import com.example.orderservice.constant.ParameterConstant;
import com.example.orderservice.dto.BillDTO;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.payload.BillRequest;
import com.example.orderservice.payload.UpdateBillRequest;
import com.example.orderservice.payload.response.CommonResponse;
import com.example.orderservice.payload.response.PageResponse;
import com.example.orderservice.service.BillService;
import com.example.orderservice.util.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {
    @Autowired
    private BillService billService;

    @PostMapping
    public ResponseEntity<CommonResponse<List<BillDTO>>> create(@RequestBody BillRequest billRequest) throws Exception {
        return ResponseUtil.wrapResponse(billService.create(billRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<BillDTO>> update(
            @PathVariable String id,
            @RequestBody UpdateBillRequest updateBillRequest) throws InvalidException, NotFoundException {
        return ResponseUtil.wrapResponse(billService.update(id, updateBillRequest));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<PageResponse<BillDTO>> getAll(
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "start") Date startAt,
            @RequestParam(name = "end") Date endAt) {
        return ResponseEntity.ok(billService.getAll(page, size, startAt, endAt));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<BillDTO>> get(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(billService.get(id));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<PageResponse<BillDTO>> getByCustomerId(
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @PathVariable String id) throws NotFoundException {
        return ResponseEntity.ok(billService.getByCustomerId(page, size, id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<CommonResponse<BillDTO>> changeStatus(
            @PathVariable String id,
            @RequestParam(name = "status") String status) throws NotFoundException, InvalidException, JsonProcessingException {
        return ResponseUtil.wrapResponse(billService.changeStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable String id) throws NotFoundException {
        billService.delete(id);
        return ResponseUtil.wrapResponse(null);
    }
}
