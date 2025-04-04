package com.example.orderservice.controller;

import com.example.orderservice.constant.I18nMessage;
import com.example.orderservice.constant.ParameterConstant;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.PageDto;
import com.example.orderservice.dto.request.OrderProductRequest;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.service.OrderService;
import com.example.servicefoundation.base.response.Response;
import com.example.servicefoundation.exception.I18nException;
import com.example.servicefoundation.i18n.I18nService;
import com.example.servicefoundation.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private I18nService i18nService;

    @PostMapping
    public ResponseEntity<Response<OrderDto>> create(@Valid @RequestBody OrderRequest orderRequest) throws Exception {
        return ResponseUtil.wrapResponse(
                orderService.create(orderRequest),
                i18nService.getMessage(I18nMessage.INFO_CREATE_ORDER, LocaleContextHolder.getLocale())
        );
    }

    @PostMapping("/product")
    public ResponseEntity<Response<OrderDto>> create(@Valid @RequestBody OrderProductRequest orderProductRequest) throws Exception {
        return ResponseUtil.wrapResponse(
                orderService.create(orderProductRequest),
                i18nService.getMessage(I18nMessage.INFO_CREATE_ORDER, LocaleContextHolder.getLocale())
        );
    }

    @PutMapping("/{orderId}/receiver/{receiverId}")
    public ResponseEntity<Response<OrderDto>> update(
            @PathVariable String orderId,
            @PathVariable String receiverId) throws I18nException {
        return ResponseUtil.wrapResponse(
                orderService.update(orderId, receiverId),
                i18nService.getMessage(I18nMessage.INFO_UPDATE_DELIVERY_INFO, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<Response<PageDto<OrderDto>>> getAll(
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "start") Date startAt,
            @RequestParam(name = "end") Date endAt,
            @RequestParam(name = "status") String status,
            @RequestParam(name = "sort-columns") List<String> sortColumns) throws Exception {
        return ResponseUtil.wrapResponse(
                orderService.getAll(page, size, startAt, endAt, status, sortColumns),
                i18nService.getMessage(I18nMessage.INFO_UPDATE_DELIVERY_INFO, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderDto>> get(@PathVariable String id) throws I18nException {
        return ResponseUtil.wrapResponse(
                orderService.get(id),
                i18nService.getMessage(I18nMessage.INFO_GET_ORDER, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/customer/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<Response<PageDto<OrderDto>>> getByCustomerId(
            @PathVariable String id,
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "sort-columns") List<String> sortColumns) {
        return ResponseUtil.wrapResponse(
                orderService.getByCustomerId(page, size, status, id, sortColumns),
                i18nService.getMessage(I18nMessage.INFO_GET_ORDER, LocaleContextHolder.getLocale())
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<Response<OrderDto>> changeStatus(
            @PathVariable String id,
            @RequestParam(name = "status") String status) throws I18nException {
        return ResponseUtil.wrapResponse(
                orderService.changeStatus(id, status),
                i18nService.getMessage(I18nMessage.INFO_GET_ORDER, LocaleContextHolder.getLocale())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable String id) throws I18nException {
        orderService.delete(id);
        return ResponseUtil.wrapResponse(
                i18nService.getMessage(I18nMessage.INFO_DELETE_ORDER, LocaleContextHolder.getLocale())
        );
    }
}
