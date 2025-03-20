package com.example.productservice.repository;

import com.example.productservice.entity.Feature;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends BaseRepository<Feature, String> {
}
