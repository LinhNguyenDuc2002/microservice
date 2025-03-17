package com.example.productservice.mybatis.mapper;

import com.example.productservice.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    int insert(Product product);
}
