package com.example.productservice.service.impl;

import com.example.productservice.constant.BillStatus;
import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.dto.CommentDTO;
import com.example.productservice.entity.Comment;
import com.example.productservice.entity.Detail;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.CommentMapper;
import com.example.productservice.payload.CommentRequest;
import com.example.productservice.repository.CommentRepository;
import com.example.productservice.repository.DetailRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CommentMapper commentMapper;
    @Override
    public CommentDTO create(String id, CommentRequest commentRequest) throws NotFoundException, InvalidException {
        Detail detail = detailRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_DETAIL_NOT_FOUND);
                });

        if(!detail.isStatus() || !detail.getBill().getStatus().equals(BillStatus.PAID)) {
            throw new InvalidException("");
        }

        Comment comment = Comment.builder()
                .message(commentRequest.getMessage())
                .allowEdit(true)
                .detail(detail)
                .product(detail.getProduct())
                .build();

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDTO> getAll(String id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
                });

        List<Comment> comments = product.getComments().stream().toList();
        return commentMapper.toDtoList(comments);
    }

    @Override
    public CommentDTO update(String id, CommentRequest commentRequest) throws NotFoundException, InvalidException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_COMMENT_NOT_FOUND);
                });

        if(!comment.isAllowEdit()) {
            throw new InvalidException("");
        }

        comment.setMessage(commentRequest.getMessage());
        comment.setAllowEdit(false);

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void delete(String id) throws NotFoundException, InvalidException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_COMMENT_NOT_FOUND);
                });

        if(!comment.isAllowEdit()) {
            throw new InvalidException("");
        }

        commentRepository.deleteById(id);
    }
}
