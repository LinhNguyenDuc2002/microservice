package com.example.productservice.service.impl;

import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.dto.CommentDTO;
import com.example.productservice.dto.MyCommentDTO;
import com.example.productservice.entity.Comment;
import com.example.productservice.entity.Customer;
import com.example.productservice.entity.Image;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.CommentMapper;
import com.example.productservice.payload.CommentRequest;
import com.example.productservice.payload.orderservice.response.CheckingDetailResponse;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.repository.CommentRepository;
import com.example.productservice.repository.CustomerRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.predicate.CommentPredicate;
import com.example.productservice.repository.predicate.CustomerPredicate;
import com.example.productservice.security.SecurityUtils;
import com.example.productservice.service.CommentService;
import com.example.productservice.service.OrderService;
import com.example.productservice.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private OrderService orderService;


    @Override
    public List<MyCommentDTO> getMyComment() throws InvalidException {
        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty()) {
            throw new InvalidException("");
        }

        CommentPredicate commentPredicate = new CommentPredicate().withAccountId(userId.get());
        List<Comment> comments = commentRepository.findAll(commentPredicate.getCriteria());

        List<MyCommentDTO> myCommentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            myCommentDTOS.add(
                    MyCommentDTO.builder()
                            .id(comment.getId())
                            .allowEdit(comment.isAllowEdit())
                            .message(comment.getMessage())
                            .images(comment.getImages().stream().map(Image::getUrl).toList())
                            .product(comment.getProduct().getId())
                            .build()
            );
        }
        return myCommentDTOS;
    }

    @Override
    public CommentDTO create(String id, CommentRequest commentRequest) throws Exception {
        CheckingDetailResponse checkingDetailResponse = orderService.checkDetailExist(id);

        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty() || userId.get() != checkingDetailResponse.getAccountId()) {
            throw new InvalidException("");
        }

        CustomerPredicate customerPredicate = new CustomerPredicate().withAccountId(userId.get());
        Customer customer = customerRepository.findOne(customerPredicate.getCriteria())
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_CUSTOMER_NOT_FOUND);
                });
        Product product = productRepository.findById(checkingDetailResponse.getProductId())
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
                });

        Comment comment = Comment.builder()
                .message(commentRequest.getMessage())
                .allowEdit(true)
                .customer(customer)
                .product(product)
                .build();

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public PageDTO<CommentDTO> getAll(String id, Integer page, Integer size, List<String> sortColumns) throws NotFoundException {
        boolean checkProduct = productRepository.existsById(id);
        if(!checkProduct) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
        }

        Pageable pageable = (sortColumns == null) ? PageUtil.getPage(page, size) : PageUtil.getPage(page, size, sortColumns.toArray(new String[0]));
        CommentPredicate commentPredicate = new CommentPredicate().withProductId(id);
        Page<Comment> comments = commentRepository.findAll(commentPredicate.getCriteria(), pageable);
        return PageDTO.<CommentDTO>builder()
                .index(comments.getNumber())
                .totalPage(comments.getTotalPages())
                .elements(commentMapper.toDtoList(comments.getContent()))
                .build();
    }

    @Override
    public CommentDTO update(String id, CommentRequest commentRequest) throws NotFoundException, InvalidException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_COMMENT_NOT_FOUND);
                });

        if (!comment.isAllowEdit()) {
            throw new InvalidException(ExceptionMessage.ERROR_COMMENT_EDIT);
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

        if (!comment.isAllowEdit()) {
            throw new InvalidException(ExceptionMessage.ERROR_COMMENT_EDIT);
        }

        commentRepository.deleteById(id);
    }
}
