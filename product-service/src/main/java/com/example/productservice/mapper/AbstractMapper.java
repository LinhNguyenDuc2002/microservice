package com.example.productservice.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public abstract class AbstractMapper<E,D> implements Mapper<E,D> {
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public D toDto(E e) {
        return modelMapper.map(e, getDtoClass());
    }

    @Override
    public E toEntity(D d) {
        return modelMapper.map(d, getEntityClass());
    }

    @Override
    public List<D> toDtoList(List<E> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities
                .stream()
                .map(this::toDto)
                .toList();
    }
}
