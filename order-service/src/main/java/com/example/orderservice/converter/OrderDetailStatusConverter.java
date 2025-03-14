package com.example.orderservice.converter;

import com.example.orderservice.constant.OrderDetailStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

@Converter
public class OrderDetailStatusConverter implements AttributeConverter<OrderDetailStatus, String> {
    @Override
    public String convertToDatabaseColumn(OrderDetailStatus orderDetailStatus) {
        return orderDetailStatus == null ? OrderDetailStatus.IN_CART.name() : orderDetailStatus.name();
    }

    @Override
    public OrderDetailStatus convertToEntityAttribute(String s) {
        if (!StringUtils.hasText(s)) {
            return OrderDetailStatus.IN_CART;
        }

        try {
            return OrderDetailStatus.valueOf(s);
        } catch (Exception e) {
//            log.warn("Invalid role type: {}", s);
            return OrderDetailStatus.IN_CART;
        }
    }
}
