package com.example.productservice.mapper;

import com.example.productservice.dto.CommentDTO;
import com.example.productservice.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper extends AbstractMapper<Comment, CommentDTO> {
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public CommentDTO toDto(Comment comment) {
        CommentDTO ret = super.toDto(comment);

        ret.setCustomer(customerMapper.toDto(comment.getDetail().getCustomer()));
        return ret;
    }

    @Override
    public Class<CommentDTO> getDtoClass() {
        return CommentDTO.class;
    }

    @Override
    public Class<Comment> getEntityClass() {
        return Comment.class;
    }
}
