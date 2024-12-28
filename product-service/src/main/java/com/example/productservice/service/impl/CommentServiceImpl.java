package com.example.productservice.service.impl;

import com.cloudinary.utils.ObjectUtils;
import com.example.productservice.constant.I18nMessage;
import com.example.productservice.dto.CommentDTO;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.request.CommentRequest;
import com.example.productservice.entity.Comment;
import com.example.productservice.entity.Image;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.InvalidationException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.CommentMapper;
import com.example.productservice.payload.orderservice.response.CheckingDetailResponse;
import com.example.productservice.repository.CommentRepository;
import com.example.productservice.repository.ImageRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.predicate.CommentPredicate;
import com.example.productservice.repository.predicate.ImagePredicate;
import com.example.productservice.security.SecurityUtils;
import com.example.productservice.service.CloudinaryService;
import com.example.productservice.service.CommentService;
import com.example.productservice.service.OrderService;
import com.example.productservice.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final Integer MAX_IMAGE_NUMBER = 5;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ImageRepository imageRepository;

//    @Override
//    public List<MyCommentDTO> getMyComment() throws InvalidationException {
//        Optional<String> userId = SecurityUtils.getLoggedInUserId();
//        if (userId.isEmpty()) {
//            throw new InvalidationException("");
//        }
//
//        CommentPredicate commentPredicate = new CommentPredicate().withAccountId(userId.get());
//        List<Comment> comments = commentRepository.findAll(commentPredicate.getCriteria());
//
//        List<MyCommentDTO> myCommentDTOS = new ArrayList<>();
//        for (Comment comment : comments) {
//            myCommentDTOS.add(
//                    MyCommentDTO.builder()
//                            .id(comment.getId())
//                            .allowEdit(comment.isAllowEdit())
//                            .message(comment.getMessage())
//                            .images(comment.getImages().stream().map(Image::getUrl).toList())
//                            .product(comment.getProduct().getId())
//                            .build()
//            );
//        }
//        return myCommentDTOS;
//    }

    @Override
    public CommentDTO create(String id, CommentRequest commentRequest) throws Exception {
        CheckingDetailResponse checkingDetailResponse = orderService.checkDetailExist(id);

        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty() || !userId.get().equals(checkingDetailResponse.getAccountId())) {
            throw new InvalidationException("");
        }

        Product product = productRepository.findById(checkingDetailResponse.getProductId())
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_PRODUCT_NOT_FOUND);
                });

        Comment comment = Comment.builder()
                .message(commentRequest.getMessage())
                .allowEdit(true)
                .customerId(userId.get())
                .product(product)
                .build();

        if (commentRequest.getImages() != null && !commentRequest.getImages().isEmpty()) {
            if (commentRequest.getImages().size() > MAX_IMAGE_NUMBER) {
                throw new ValidateException(I18nMessage.ERROR_IMAGE_NUMBER_EXCEEDED);
            } else {
//                List<Image> images = cloudinaryService.uploadImage(commentRequest.getImages(), ObjectUtils.asMap());
//                images.stream().forEach(image -> {
//                    image.setComment(comment);
//                });
//                comment.setImages(images);
            }
        }
        commentRepository.save(comment);

        return commentMapper.toDto(comment);
    }

    @Override
    public PageDTO<CommentDTO> getAll(String id, Integer page, Integer size, List<String> sortColumns) throws NotFoundException {
        boolean checkProduct = productRepository.existsById(id);
        if (!checkProduct) {
            throw new NotFoundException(I18nMessage.ERROR_PRODUCT_NOT_FOUND);
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
    public CommentDTO update(String id, CommentRequest commentRequest) throws NotFoundException, InvalidationException, IOException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_COMMENT_NOT_FOUND);
                });

        if (!comment.isAllowEdit()) {
            throw new InvalidationException(I18nMessage.ERROR_COMMENT_EDIT);
        }

        if (commentRequest.getImages() != null && !commentRequest.getImages().isEmpty()) {
            if (commentRequest.getImages().size() > MAX_IMAGE_NUMBER) {
                throw new ValidateException(I18nMessage.ERROR_IMAGE_NUMBER_EXCEEDED);
            } else {
                ImagePredicate imagePredicate = new ImagePredicate().withCommentId(id);
                List<String> ids = imageRepository.findAll(imagePredicate.getCriteria()).stream()
                        .map(Image::getId).toList();
                cloudinaryService.destroy(ids);

//                List<Image> images = cloudinaryService.uploadImage(commentRequest.getImages(), ObjectUtils.asMap());
//                images.stream().forEach(image -> {
//                    image.setComment(comment);
//                });
//                comment.setImages(images);
            }
        }

        comment.setMessage(commentRequest.getMessage());
        comment.setAllowEdit(false);
        commentRepository.save(comment);

        return commentMapper.toDto(comment);
    }

    @Override
    public void delete(String id) throws NotFoundException, InvalidationException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_COMMENT_NOT_FOUND);
                });

        if (!comment.isAllowEdit()) {
            throw new InvalidationException(I18nMessage.ERROR_COMMENT_EDIT);
        }

        commentRepository.deleteById(id);
    }
}
