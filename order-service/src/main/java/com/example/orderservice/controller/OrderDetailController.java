package com.example.orderservice.controller;

import com.example.orderservice.constant.I18nMessage;
import com.example.orderservice.constant.ParameterConstant;
import com.example.orderservice.dto.OrderDetailDto;
import com.example.orderservice.dto.PageDto;
import com.example.orderservice.exception.InvalidationException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.service.OrderDetailService;
import com.example.servicefoundation.base.response.Response;
import com.example.servicefoundation.i18n.I18nService;
import com.example.servicefoundation.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-detail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private I18nService i18nService;

    @PostMapping
    public ResponseEntity<Response<OrderDetailDto>> create(
            @RequestParam(name = "product", required = true) String productDetailId,
            @RequestParam(name = "quantity", defaultValue = ParameterConstant.Quantity.DEFAULT_MIN_QUANTITY) Integer quantity) throws Exception {
        return ResponseUtil.wrapResponse(
                orderDetailService.create(productDetailId, quantity),
                i18nService.getMessage(I18nMessage.INFO_ADD_ORDER_DETAIL, LocaleContextHolder.getLocale())
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response<OrderDetailDto>> update(
            @PathVariable String id,
            @RequestParam(name = "quantity", required = false) Integer quantity) throws Exception {
        return ResponseUtil.wrapResponse(
                orderDetailService.update(id, quantity),
                i18nService.getMessage(I18nMessage.INFO_UPDATE_ORDER_DETAIL, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping
    public ResponseEntity<Response<PageDto<OrderDetailDto>>> getAll(
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "customer", required = false) String customerId,
            @RequestParam(name = "status", required = false) String status) throws Exception {
        return ResponseUtil.wrapResponse(
                orderDetailService.getAll(page, size, customerId, status),
                i18nService.getMessage(I18nMessage.INFO_GET_ORDER_DETAIL, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderDetailDto>> get(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(
                orderDetailService.get(id),
                i18nService.getMessage(I18nMessage.INFO_GET_ORDER_DETAIL, LocaleContextHolder.getLocale())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable String id) throws NotFoundException, InvalidationException {
        orderDetailService.delete(id);
        return ResponseUtil.wrapResponse(
                i18nService.getMessage(I18nMessage.INFO_DELETE_ORDER_DETAIL, LocaleContextHolder.getLocale())
        );
    }
}
