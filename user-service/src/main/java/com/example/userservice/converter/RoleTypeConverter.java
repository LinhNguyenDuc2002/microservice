package com.example.userservice.converter;

import com.example.userservice.constant.RoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Convert RoleType object and String
 * when saving in DB, convert RoleType to String
 * when mapping to Role entity, convert String to RoleType
 */
@Converter
@Slf4j
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {
    @Override
    public String convertToDatabaseColumn(RoleType roleType) {
        return roleType == null ? RoleType.CUSTOMER.name() : roleType.name();
    }

    @Override
    public RoleType convertToEntityAttribute(String s) {
        if(!StringUtils.hasText(s)) {
            return RoleType.CUSTOMER;
        }
        try {
            return RoleType.valueOf(s);
        } catch (Exception e) {
            log.warn("Invalid role type: {}", s);
            return RoleType.CUSTOMER;
        }
    }
}
