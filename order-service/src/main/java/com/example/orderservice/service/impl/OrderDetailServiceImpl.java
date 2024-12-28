package com.example.orderservice.service.impl;

import com.example.orderservice.constant.I18nMessage;
import com.example.orderservice.constant.OrderDetailStatus;
import com.example.orderservice.dto.OrderDetailDto;
import com.example.orderservice.dto.PageDto;
import com.example.orderservice.entity.OrderDetail;
import com.example.orderservice.exception.InvalidationException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.exception.UnauthorizedException;
import com.example.orderservice.mapper.OrderDetailMapper;
import com.example.orderservice.payload.productservice.response.ProductCheckingResponse;
import com.example.orderservice.repository.OrderDetailRepository;
import com.example.orderservice.repository.predicate.OrderDetailPredicate;
import com.example.orderservice.security.SecurityUtil;
import com.example.orderservice.service.OrderDetailService;
import com.example.orderservice.service.ProductService;
import com.example.orderservice.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
            throw new UnauthorizedException(I18nMessage.ERROR_UNAUTHORIZED);
        }

        if (quantity <= 0) {
            throw new InvalidationException(I18nMessage.ERROR_QUANTITY_MIN);
        }

        ProductCheckingResponse productCheckingResponse = productService.checkProductExist(productDetailId, quantity);
        if (!productCheckingResponse.isExist()) {
            throw new NotFoundException(productCheckingResponse.getInfo());
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
                    return new NotFoundException(I18nMessage.ERROR_ORDER_DETAIL_NOT_FOUND);
                });

        if (quantity <= 0) {
            throw new InvalidationException(I18nMessage.ERROR_QUANTITY_MIN);
        }

        ProductCheckingResponse productCheckingResponse = productService.checkProductExist(orderDetail.getProductDetailId(), quantity);
        if (!productCheckingResponse.isExist()) {
            throw new NotFoundException(productCheckingResponse.getInfo());
        }

        orderDetail.setQuantity(quantity);
        orderDetailRepository.save(orderDetail);

        return orderDetailMapper.toDto(orderDetail);
    }

    @Override
    public PageDto<OrderDetailDto> getAll(Integer page, Integer size, String customerId, String status) throws Exception {
        Pageable pageable = PageUtil.getPage(page, size);

        OrderDetailPredicate orderDetailPredicate = new OrderDetailPredicate()
                .withStatus(OrderDetailStatus.valueOf(status))
                .withCustomerId(customerId);
        Page<OrderDetail> details = orderDetailRepository.findAll(orderDetailPredicate.getCriteria(), pageable);

        return PageDto.<OrderDetailDto>builder()
                .index(page)
                .totalPage(details.getTotalPages())
                .elements(orderDetailMapper.toDtoList(details.getContent()))
                .build();
    }

    @Override
    public OrderDetailDto get(String id) throws NotFoundException {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_ORDER_DETAIL_NOT_FOUND);
                });

        return orderDetailMapper.toDto(orderDetail);
    }

    @Override
    public void delete(String id) throws NotFoundException, InvalidationException {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_ORDER_DETAIL_NOT_FOUND);
                });

        if (orderDetail.getStatus().equals(OrderDetailStatus.PURCHASED)) {
            throw new InvalidationException(I18nMessage.ERROR_ORDER_CAN_NOT_DELETE);
        }
        orderDetailRepository.deleteById(id);
    }
}
