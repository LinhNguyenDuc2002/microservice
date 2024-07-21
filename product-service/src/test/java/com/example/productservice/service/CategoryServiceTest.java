package com.example.productservice.service;

import com.example.productservice.BaseTest;
import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.entity.Category;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mock.CategoryMock;
import com.example.productservice.payload.CategoryRequest;
import com.example.productservice.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class CategoryServiceTest extends BaseTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Override
    protected void cleanAfterEach() {
        categoryRepository.deleteAll();
    }

    @Override
    protected void cleanBeforeEach() {
        categoryRepository.deleteAll();
    }

    @Test
    void test_add() throws InvalidException {
        // Input
        CategoryRequest categoryRequest = CategoryMock.mockCategoryRequest();

        // Test
        categoryService.add(categoryRequest);

        // Expect
        assertEquals(1, categoryRepository.findAll().size());
    }

    @Test
    void test_add_throwInvalidException() throws InvalidException {
        assertThrows(InvalidException.class, () -> categoryService.add(null));
    }

    @Test
    void test_update() throws InvalidException, NotFoundException {
        mockCategory();
        String categoryId = categoryRepository.findAll().get(0).getId();
        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("Cloth")
                .note("test")
                .build();

        categoryService.update(categoryId, categoryRequest);

        // Expect
        Category category = categoryRepository.findAll().get(0);
        assertEquals(categoryRequest.getName(), category.getName());
        assertEquals(categoryRequest.getNote(), category.getNote());
    }

    @Test
    void test_update_throwException() {
        // test1
        assertThrows(NotFoundException.class, () -> categoryService.update(UUID.randomUUID().toString(), null));

        // test2
        mockCategory();
        String categoryId = categoryRepository.findAll().get(0).getId();

        assertThrows(InvalidException.class, () -> categoryService.update(categoryId, null));
    }

    @Test
    void test_getAll() throws NotFoundException {
        mockCategory();

        List<CategoryDTO> response = categoryService.getAll();

        assertEquals(1, response.size());
    }

    @Test
    void test_delete() throws NotFoundException {
        mockCategory();
        String categoryId = categoryRepository.findAll().get(0).getId();

        categoryService.delete(categoryId);

        assertEquals(0, categoryRepository.findAll().size());
    }

    @Test
    void test_delete_throwNotFoundException() throws NotFoundException {
        assertThrows(NotFoundException.class, () -> categoryService.delete(UUID.randomUUID().toString()));
    }

    private void mockCategory() {
        Category category = CategoryMock.mockCategory();
        categoryRepository.save(category);
    }
}
