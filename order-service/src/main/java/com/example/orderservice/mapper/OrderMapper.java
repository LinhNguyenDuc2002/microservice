package com.example.orderservice.mapper;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper extends AbstractMapper<Order, OrderDto> {
    @Override
    public Class<OrderDto> getDtoClass() {
        return OrderDto.class;
    }

    @Override
    public Class<Order> getEntityClass() {
        return Order.class;
    }
}
