package com.example.productservice.mapper;

import java.util.List;

/**
 *
 * @param <E>
 * @param <D>
 */
public interface Mapper<E,D> {
    /**
     *
     * @param e
     * @return
     */
    D toDto(E e);

    /**
     *
     * @param d
     * @return
     */
    E toEntity(D d);

    /**
     *
     * @param entities
     * @return
     */
    List<D> toDtoList(List<E> entities);

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
