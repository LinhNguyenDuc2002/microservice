package com.example.productservice.mapper;

import com.example.productservice.dto.DetailDTO;
import com.example.productservice.entity.Detail;
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
