package com.example.orderservice.converter;

import com.example.orderservice.constant.OrderStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Converter
@Slf4j
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
    @Override
    public String convertToDatabaseColumn(OrderStatus orderStatus) {
        return orderStatus == null ? OrderStatus.PROCESSING.name() : orderStatus.name();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String s) {
        if (!StringUtils.hasText(s)) {
            return OrderStatus.PROCESSING;
        }
        try {
            return OrderStatus.valueOf(s);
        } catch (Exception e) {
            log.warn("Invalid role type: {}", s);
            return OrderStatus.PROCESSING;
        }
    }
}
