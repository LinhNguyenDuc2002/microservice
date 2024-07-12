package com.example.orderservice.mapper;

import com.example.orderservice.dto.DetailDTO;
import com.example.orderservice.entity.Detail;
import org.springframework.stereotype.Component;

@Component
public class DetailMapper extends AbstractMapper<Detail, DetailDTO> {
    @Override
    public Class<DetailDTO> getDtoClass() {
        return DetailDTO.class;
    }

    @Override
    public Class<Detail> getEntityClass() {
        return Detail.class;
    }
}
