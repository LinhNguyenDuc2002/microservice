package com.example.productservice.service.impl;

import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.dto.CommentDTO;
import com.example.productservice.entity.Comment;
import com.example.productservice.entity.Customer;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.CommentMapper;
import com.example.productservice.payload.CommentRequest;
import com.example.productservice.payload.orderservice.response.CheckingDetailResponse;
import com.example.productservice.repository.CommentRepository;
import com.example.productservice.repository.CustomerRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.predicate.CustomerPredicate;
import com.example.productservice.service.CommentService;
import com.example.productservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CommentDTO create(String id, CommentRequest commentRequest) throws Exception {
//        CheckingDetailResponse checkingDetailResponse = orderService.checkDetailExist(id);
//
//        CustomerPredicate customerPredicate = new CustomerPredicate().withAccountId(checkingDetailResponse.getCustomer());
//        Optional<Customer> customer = customerRepository.findOne(customerPredicate.getCriteria());
//        Optional<Product> product = productRepository.findById(checkingDetailResponse.getProduct());
//
//        Comment comment = Comment.builder()
//                .message(commentRequest.getMessage())
//                .allowEdit(true)
//                .customer(customer.get())
//                .product(product.get())
//                .build();
//
//        return commentMapper.toDto(commentRepository.save(comment));
        return null;
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

        if(!comment.isAllowEdit()) {
            throw new InvalidException(ExceptionMessage.ERROR_COMMENT_EDIT);
        }

        commentRepository.deleteById(id);
    }
}
