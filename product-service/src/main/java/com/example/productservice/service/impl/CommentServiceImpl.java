package com.example.productservice.service.impl;

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
import com.example.productservice.payload.userservice.response.CustomerInfoResponse;
import com.example.productservice.repository.CommentRepository;
import com.example.productservice.repository.ImageRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.predicate.CommentPredicate;
import com.example.productservice.security.SecurityUtils;
import com.example.productservice.service.CloudinaryService;
import com.example.productservice.service.CommentService;
import com.example.productservice.service.OrderService;
import com.example.productservice.service.UserService;
import com.example.productservice.util.PageUtil;
import com.example.productservice.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    @Autowired
    private UserService userService;

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
        Comment parentComment = null;
        if(StringUtils.hasText(commentRequest.getParentId())) {
            parentComment = commentRepository.findById(commentRequest.getParentId())
                    .orElseThrow(() -> {
                        return new NotFoundException(I18nMessage.ERROR_COMMENT_NOT_FOUND);
                    });
        }
        CheckingDetailResponse checkingDetailResponse = orderService.checkDetailExist(id);

        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty() || !userId.get().equals(checkingDetailResponse.getAccountId())) {
            throw new InvalidationException("");
        }

        Product product = productRepository.findById(checkingDetailResponse.getProductId())
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_PRODUCT_NOT_FOUND);
                });

        Map<String, MultipartFile> images = new HashMap<>();
        if (commentRequest.getImages() != null && !commentRequest.getImages().isEmpty()) {
            long totalSize = 0;
            for (MultipartFile file : commentRequest.getImages()) {
                totalSize += file.getSize();
                images.put(UUID.randomUUID().toString(), file);
            }
            double totalSizeInMB = totalSize / (1024 * 1024);
            if (commentRequest.getImages().size() > MAX_IMAGE_NUMBER || totalSizeInMB > 4) {
                throw new ValidateException(I18nMessage.ERROR_IMAGE_NUMBER_EXCEEDED);
            }

            cloudinaryService.upload(images);
        }

        Comment comment = Comment.builder()
                .message(commentRequest.getMessage())
                .allowEdit(true)
                .imageIds(StringUtil.joinDelimiter(images.keySet().stream().toList()))
                .customerId(userId.get())
                .product(product)
                .build();
        if(parentComment != null) {
            comment.setParentComment(parentComment);
        }

        commentRepository.save(comment);

        return commentMapper.toDto(comment);
    }

    @Override
    public PageDTO<CommentDTO> getAll(String id, Integer page, Integer size, List<String> sortColumns) throws Exception {
        boolean checkProduct = productRepository.existsById(id);
        if (!checkProduct) {
            throw new NotFoundException(I18nMessage.ERROR_PRODUCT_NOT_FOUND);
        }

        Pageable pageable = (sortColumns == null) ? PageUtil.getPage(page, size) : PageUtil.getPage(page, size, sortColumns.toArray(new String[0]));
        CommentPredicate commentPredicate = new CommentPredicate().withProductId(id);
        Page<Comment> comments = commentRepository.findAll(commentPredicate.getCriteria(), pageable);
        List<String> ids = comments.getContent().stream().map(Comment::getCustomerId).distinct().toList();
        Map<String, CustomerInfoResponse> customerResponses = userService.getUserInfo(ids);

        List<CommentDTO> commentDTOS = new ArrayList<>();
        for(Comment comment : comments) {
            CommentDTO commentDTO = commentMapper.toDto(comment);
            List<Image> images = imageRepository.findAllById(Collections.singleton(comment.getImageIds()));
            List<String> urls = images.stream().map(Image::getSecureUrl).toList();

            commentDTO.setImageUrls(urls);
            commentDTO.setCustomer(customerResponses.get(comment.getCustomerId()));
        }

        return PageDTO.<CommentDTO>builder()
                .index(comments.getNumber())
                .totalPage(comments.getTotalPages())
                .elements(commentDTOS)
                .build();
    }

    @Override
    public PageDTO<CommentDTO> getAllChild(String id, Integer page, Integer size, List<String> sortColumns) throws Exception {
        Pageable pageable = (sortColumns == null) ? PageUtil.getPage(page, size) : PageUtil.getPage(page, size, sortColumns.toArray(new String[0]));
        CommentPredicate commentPredicate = new CommentPredicate().withParentId(id);
        Page<Comment> comments = commentRepository.findAll(commentPredicate.getCriteria(), pageable);
        List<String> ids = comments.getContent().stream().map(Comment::getCustomerId).distinct().toList();
        Map<String, CustomerInfoResponse> customerResponses = userService.getUserInfo(ids);

        List<CommentDTO> commentDTOS = new ArrayList<>();
        for(Comment comment : comments) {
            CommentDTO commentDTO = commentMapper.toDto(comment);
            List<Image> images = imageRepository.findAllById(Collections.singleton(comment.getImageIds()));
            List<String> urls = images.stream().map(Image::getSecureUrl).toList();

            commentDTO.setImageUrls(urls);
            commentDTO.setCustomer(customerResponses.get(comment.getCustomerId()));
        }

        return PageDTO.<CommentDTO>builder()
                .index(comments.getNumber())
                .totalPage(comments.getTotalPages())
                .elements(commentDTOS)
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

        Map<String, MultipartFile> images = new HashMap<>();
        if (commentRequest.getImages() != null && !commentRequest.getImages().isEmpty()) {
            long totalSize = 0;
            for (MultipartFile file : commentRequest.getImages()) {
                totalSize += file.getSize();
                images.put(UUID.randomUUID().toString(), file);
            }
            double totalSizeInMB = totalSize / (1024 * 1024);
            if (commentRequest.getImages().size() > MAX_IMAGE_NUMBER || totalSizeInMB > 4) {
                throw new ValidateException(I18nMessage.ERROR_IMAGE_NUMBER_EXCEEDED);
            }

            cloudinaryService.upload(images);
        }

        comment.setImageIds(StringUtil.joinDelimiter(images.keySet().stream().toList()));
        comment.setMessage(commentRequest.getMessage());
        comment.setAllowEdit(false);
        commentRepository.save(comment);

        return commentMapper.toDto(comment);
    }

    @Override
    public void delete(String id) throws NotFoundException, InvalidationException, IOException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_COMMENT_NOT_FOUND);
                });

        if (!comment.isAllowEdit()) {
            throw new InvalidationException(I18nMessage.ERROR_COMMENT_EDIT);
        }

        List<String> ids = comment.getChildComments().stream().map(Comment::getId).toList();
        ids.add(id);
        commentRepository.deleteAllById(ids);
        cloudinaryService.destroy(StringUtil.splitDelimiter(comment.getImageIds()));
    }
}
