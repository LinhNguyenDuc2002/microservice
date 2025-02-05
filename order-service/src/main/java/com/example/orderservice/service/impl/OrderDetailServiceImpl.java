package com.example.orderservice.service.impl;

import com.example.orderservice.constant.I18nMessage;
import com.example.orderservice.constant.OrderDetailStatus;
import com.example.orderservice.dto.OrderDetailDto;
import com.example.orderservice.dto.PageDto;
import com.example.orderservice.entity.OrderDetail;
import com.example.orderservice.mapper.OrderDetailMapper;
import com.example.orderservice.payload.productservice.response.ProductCheckingResponse;
import com.example.orderservice.repository.OrderDetailRepository;
import com.example.orderservice.repository.predicate.OrderDetailPredicate;
import com.example.orderservice.security.SecurityUtil;
import com.example.orderservice.service.OrderDetailService;
import com.example.orderservice.service.ProductService;
import com.example.servicefoundation.exception.I18nException;
import com.example.servicefoundation.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ProductService productService;

    @Override
    public OrderDetailDto create(String productDetailId, Integer quantity) throws Exception {
        Optional<String> userId = SecurityUtil.getLoggedInUserId();
        if (!userId.isPresent()) {
            throw I18nException.builder()
                    .code(HttpStatus.UNAUTHORIZED)
                    .message(I18nMessage.ERROR_UNAUTHORIZED)
                    .build();
        }

        OrderDetailPredicate orderDetailPredicate = new OrderDetailPredicate()
                .withProductDetailId(productDetailId)
                .withCustomerId(userId.get())
                .withStatus(OrderDetailStatus.IN_CART);
        List<OrderDetail> check = orderDetailRepository.findAll(orderDetailPredicate.getCriteria());
        if (!check.isEmpty()) {
            OrderDetail existOrderDetail = check.get(0);
            ProductCheckingResponse productCheckingResponse = productService.checkProductExist(productDetailId, quantity + existOrderDetail.getQuantity());
            if (productCheckingResponse.isExist()) {
                existOrderDetail.setQuantity(quantity + existOrderDetail.getQuantity());
                existOrderDetail.setUnitPrice(productCheckingResponse.getPrice());
                orderDetailRepository.save(existOrderDetail);
            }

            return orderDetailMapper.toDto(existOrderDetail);
        }

        if (quantity <= 0) {
            throw I18nException.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(I18nMessage.ERROR_QUANTITY_MIN)
                    .build();
        }

        ProductCheckingResponse productCheckingResponse = productService.checkProductExist(productDetailId, quantity);
        if (!productCheckingResponse.isExist()) {
            throw I18nException.builder()
                    .code(HttpStatus.NOT_FOUND)
                    .object(productCheckingResponse.getInfo())
                    .build();
        }

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(quantity)
                .unitPrice(productCheckingResponse.getPrice())
                .productDetailId(productDetailId)
                .customerId(userId.get())
                .status(OrderDetailStatus.IN_CART)
                .commentStatus(false)
                .build();
        orderDetailRepository.save(orderDetail);

        return orderDetailMapper.toDto(orderDetail);
    }

    @Override
    public OrderDetailDto update(String id, Integer quantity) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_ORDER_DETAIL_NOT_FOUND)
                            .build();
                });

        if (quantity <= 0) {
            throw I18nException.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(I18nMessage.ERROR_QUANTITY_MIN)
                    .build();
        }

        ProductCheckingResponse productCheckingResponse = productService.checkProductExist(orderDetail.getProductDetailId(), quantity);
        if (!productCheckingResponse.isExist()) {
            throw I18nException.builder()
                    .code(HttpStatus.NOT_FOUND)
                    .object(productCheckingResponse.getInfo())
                    .build();
        }

        orderDetail.setQuantity(quantity);
        orderDetailRepository.save(orderDetail);

        return orderDetailMapper.toDto(orderDetail);
    }

    @Override
    public PageDto<OrderDetailDto> getAll(Integer page, Integer size, String customerId, String status) throws Exception {
        Pageable pageable = PaginationUtil.getPage(page, size);

        OrderDetailStatus orderDetailStatus = null;
        try {
            if (StringUtils.hasText(status)) {
                orderDetailStatus = OrderDetailStatus.valueOf(status);
            }
        } catch (IllegalArgumentException e) {
            orderDetailStatus = null;
        }
        OrderDetailPredicate orderDetailPredicate = new OrderDetailPredicate()
                .withStatus(orderDetailStatus)
                .withCustomerId(customerId);
        Page<OrderDetail> details = orderDetailRepository.findAll(orderDetailPredicate.getCriteria(), pageable);

        return PageDto.<OrderDetailDto>builder()
                .index(page)
                .totalPage(details.getTotalPages())
                .elements(orderDetailMapper.toDtoList(details.getContent()))
                .build();
    }

    @Override
    public OrderDetailDto get(String id) throws I18nException {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_ORDER_DETAIL_NOT_FOUND)
                            .build();
                });

        return orderDetailMapper.toDto(orderDetail);
    }

    @Override
    public void delete(String id) throws I18nException {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_ORDER_DETAIL_NOT_FOUND)
                            .build();
                });

        if (orderDetail.getStatus().equals(OrderDetailStatus.PURCHASED)) {
            throw I18nException.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(I18nMessage.ERROR_ORDER_CAN_NOT_DELETE)
                    .build();
        }
        orderDetailRepository.deleteById(id);
    }
}
