package com.example.productservice.repository;

import com.example.productservice.entity.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends BaseRepository<Comment, String> {
}
