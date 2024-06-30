package com.example.userservice.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Converter
@Slf4j
public class StringListConverter implements AttributeConverter<Collection<String>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(Collection<String> strings) {
        return strings == null ? null : String.join(SPLIT_CHAR, strings);
    }

    @Override
    public Collection<String> convertToEntityAttribute(String s) {
        if(StringUtils.hasText(s)) {
            return Arrays.asList(s.split(SPLIT_CHAR));
        }

        return Collections.emptyList();
    }
}
