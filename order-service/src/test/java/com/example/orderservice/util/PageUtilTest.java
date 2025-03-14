package com.example.orderservice.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageUtilTest {
    private final Integer page = 1;

    private final Integer size = 10;
    @Test
    void test_getPage() {
        assertEquals(PageRequest.of(page, size), PageUtil.getPage(page, size));
    }

    @Test
    void test_getPage_isNull() {
        assertEquals(null, PageUtil.getPage(page, null));
        assertEquals(null, PageUtil.getPage(null, size));
    }
}
