package com.example.productservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Objects;

@Slf4j
public final class PageUtil {
    public static Pageable getPage(Integer page, Integer size) {
        if (Objects.nonNull(page) && Objects.nonNull(size)) {
            return PageRequest.of(page, size);
        }

        log.info("Page and size are null");
        return null;
    }
}
