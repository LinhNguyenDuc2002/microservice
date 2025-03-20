package com.example.productservice.mapper;

import com.example.productservice.dto.FeatureDTO;
import com.example.productservice.entity.Feature;
import org.springframework.stereotype.Component;

@Component
public class FeatureMapper extends AbstractMapper<Feature, FeatureDTO> {
    @Override
    public Class<FeatureDTO> getDtoClass() {
        return FeatureDTO.class;
    }

    @Override
    public Class<Feature> getEntityClass() {
        return Feature.class;
    }
}
