package com.example.productservice.repository;

import com.example.productservice.entity.Category;
import com.example.productservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends BaseRepository<Category, String> {
}
