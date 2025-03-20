package com.example.productservice.repository;

import com.example.productservice.entity.Attribute;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends BaseRepository<Attribute, String> {
    List<Attribute> findByIdAndFeatureId(String id, String featureId);
}
