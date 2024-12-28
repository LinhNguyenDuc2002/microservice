package com.example.orderservice.mapper;

import com.example.orderservice.dto.OrderDetailDto;
import com.example.orderservice.entity.OrderDetail;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailMapper extends AbstractMapper<OrderDetail, OrderDetailDto> {
    @Override
    public Class<OrderDetailDto> getDtoClass() {
        return OrderDetailDto.class;
    }

    @Override
    public Class<OrderDetail> getEntityClass() {
        return OrderDetail.class;
    }
}
