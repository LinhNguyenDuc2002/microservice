package com.example.productservice.mapper;

import com.example.productservice.dto.CommentDTO;
import com.example.productservice.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper extends AbstractMapper<Comment, CommentDTO> {
    @Override
    public Class<CommentDTO> getDtoClass() {
        return CommentDTO.class;
    }

    @Override
    public Class<Comment> getEntityClass() {
        return Comment.class;
    }
}
