package com.example.productservice.mapper;

import com.example.productservice.dto.CommentDTO;
import com.example.productservice.entity.Comment;
import com.example.productservice.entity.Image;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CommentMapper extends AbstractMapper<Comment, CommentDTO> {
    @Override
    public CommentDTO toDto(Comment comment) {
        CommentDTO commentDTO = super.toDto(comment);

        if (comment.getImages() != null && !comment.getImages().isEmpty()) {
            commentDTO.setImageUrls(
                    comment.getImages().stream()
                            .map(Image::getSecureUrl)
                            .collect(Collectors.toList())
            );
        }

        return commentDTO;
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
