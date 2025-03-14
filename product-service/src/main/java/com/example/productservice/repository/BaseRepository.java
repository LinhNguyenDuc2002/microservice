package com.example.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * E
 * I
 */
@NoRepositoryBean
public interface BaseRepository<E,I> extends JpaRepository<E,I>, ListQuerydslPredicateExecutor<E> {

}
