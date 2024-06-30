package com.example.userservice.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public abstract class AbstractMapper<E, D> implements Mapper<E, D> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public E toEntity(D d) {
        return modelMapper.map(d, getEntityClass());
    }

    @Override
    public D toDto(E e) {
        if (e == null) {
            return null;
        }
        return modelMapper.map(e, getDtoClass());
    }

    @Override
    public List<D> toDtoList(List<E> e) {
        if (e == null || e.isEmpty()) {
            return Collections.emptyList();
        }

        return e.stream()
                .map(this::toDto)
                .toList();
    }
}
