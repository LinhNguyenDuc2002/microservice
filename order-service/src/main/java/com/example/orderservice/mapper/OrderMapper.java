package com.example.orderservice.mapper;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.PurchaseOrder;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper extends AbstractMapper<PurchaseOrder, OrderDto> {
    @Override
    public Class<OrderDto> getDtoClass() {
        return OrderDto.class;
    }

    @Override
    public Class<PurchaseOrder> getEntityClass() {
        return PurchaseOrder.class;
    }
}
