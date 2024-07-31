package com.example.productservice.service;

import com.example.productservice.BaseTest;
import com.example.productservice.dto.CommentDTO;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.entity.Comment;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mock.CommentMock;
import com.example.productservice.mock.ProductMock;
import com.example.productservice.payload.CommentRequest;
import com.example.productservice.repository.CommentRepository;
import com.example.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class CommentServiceTest extends BaseTest {
    @Autowired
    private CommentService commentService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    protected void cleanAfterEach() {
        productRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Override
    protected void cleanBeforeEach() {
        productRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @Transactional
    void test_getAll() throws NotFoundException {
        mockProduct();
        Product product = productRepository.findAll().get(0);
        mockComment(product, true);

        PageDTO<CommentDTO> response = commentService.getAll(product.getId(), 0, 10, null);

        assertEquals(1, response.getElements().size());
        assertEquals(0, response.getIndex());
    }

    @Test
    void test_getAll_throwException() {
        assertThrows(NotFoundException.class, () -> commentService.getAll(UUID.randomUUID().toString(), 0, 10, null));
    }

    @Test
    @Transactional
    void test_update() throws NotFoundException, InvalidException {
        mockProduct();
        Product product = productRepository.findAll().get(0);
        mockComment(product, true);
        String commentId = commentRepository.findAll().get(0).getId();

        CommentRequest commentRequest = new CommentRequest("Updated message");
        CommentDTO response = commentService.update(commentId, commentRequest);

        assertEquals("Updated message", response.getMessage());
    }

    @Test
    @Transactional
    void test_update_throwException() throws NotFoundException, InvalidException {
        // Test 1
        assertThrows(NotFoundException.class, () -> commentService.update(UUID.randomUUID().toString(), null));

        // Test 2
        mockProduct();
        Product product = productRepository.findAll().get(0);
        mockComment(product, false);
        String commentId = commentRepository.findAll().get(0).getId();

        assertThrows(InvalidException.class, () -> commentService.update(commentId, null));
    }

    @Test
    @Transactional
    void test_delete() throws NotFoundException, InvalidException {
        mockProduct();
        Product product = productRepository.findAll().get(0);
        mockComment(product, true);
        String commentId = commentRepository.findAll().get(0).getId();

        commentService.delete(commentId);

        assertEquals(0, commentRepository.findAll().size());
    }

    @Test
    @Transactional
    void test_delete_throwException() throws NotFoundException, InvalidException {
        // Test 1
        assertThrows(NotFoundException.class, () -> commentService.delete(UUID.randomUUID().toString()));

        // Test 2
        mockProduct();
        Product product = productRepository.findAll().get(0);
        mockComment(product, false);
        String commentId = commentRepository.findAll().get(0).getId();

        assertThrows(InvalidException.class, () -> commentService.delete(commentId));
    }

    private void mockProduct() {
        productRepository.save(ProductMock.mockProduct());
    }

    private void mockComment(Product product, boolean allowEdit) {
        Comment comment = CommentMock.mockComment(allowEdit);
        comment.setProduct(product);
        commentRepository.save(comment);
    }

}
