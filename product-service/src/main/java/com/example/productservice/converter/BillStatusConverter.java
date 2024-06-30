package com.example.productservice.converter;

import com.example.productservice.constant.BillStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Converter
@Slf4j
public class BillStatusConverter implements AttributeConverter<BillStatus, String> {
    @Override
    public String convertToDatabaseColumn(BillStatus billStatus) {
        return billStatus == null ? BillStatus.PROCESSING.name() : billStatus.name();
    }

    @Override
    public BillStatus convertToEntityAttribute(String s) {
        if (!StringUtils.hasText(s)) {
            return BillStatus.PROCESSING;
        }
        try {
            return BillStatus.valueOf(s);
        } catch (Exception e) {
            log.warn("Invalid role type: {}", s);
            return BillStatus.PROCESSING;
        }
    }
}
