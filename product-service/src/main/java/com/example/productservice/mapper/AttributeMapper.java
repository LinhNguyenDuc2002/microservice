package com.example.productservice.mapper;

import com.example.productservice.dto.AttributeDTO;
import com.example.productservice.entity.Attribute;
import org.springframework.stereotype.Component;

@Component
public class AttributeMapper extends AbstractMapper<Attribute, AttributeDTO> {
    @Override
    public Class<AttributeDTO> getDtoClass() {
        return AttributeDTO.class;
    }

    @Override
    public Class<Attribute> getEntityClass() {
        return Attribute.class;
    }
}
