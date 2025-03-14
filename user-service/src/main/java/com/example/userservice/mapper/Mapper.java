package com.example.userservice.mapper;

import java.util.List;

public interface Mapper<E, D> {

    /**
     *
     * @param d
     * @return
     */
    E toEntity(D d);

    /**
     *
     * @param e
     * @return
     */
    D toDto(E e);

    /**
     *
     * @param e
     * @return
     */
    List<D> toDtoList(List<E> e);

    /**
     *
     * @return
     */
    Class<D> getDtoClass();

    /**
     *
     * @return
     */
    Class<E> getEntityClass();
}
