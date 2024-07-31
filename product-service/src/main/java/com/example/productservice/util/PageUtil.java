package com.example.productservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * Init pagination request
     *
     * @param page          page number
     * @param size          size of page
     * @param sortColumns   list of sort columns in format: <column_name>:<direction> (such as: id:asc, name:desc) or column name only
     */
    public static Pageable getPage(Integer page, Integer size, String... sortColumns) {
        if (Objects.nonNull(page) && Objects.nonNull(size)) {
            return PageRequest.of(page, size, Sorting.by(sortColumns));
        }

        log.info("Page and size are null");
        return null;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Slf4j
    public static final class Sorting {
        public static final String DELIMITER = ":";

        public static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.ASC;

        public static Sort by(String... sortColumns) {
            List<Sort.Order> orders = new ArrayList<>();
            for (String order : sortColumns) {
                String[] parts = order.trim().split(DELIMITER);
                String property = parts[0];
                Sort.Direction direction = DEFAULT_DIRECTION;

                if (parts.length == 2) {
                    try {
                        direction = Sort.Direction.fromString(parts[1].trim());
                    } catch (Exception e) {
                        log.error("Invalid sort direction: {}", parts[1]);
                        continue;
                    }
                }

                orders.add(new Sort.Order(direction, property));
            }

            return Sort.by(orders);
        }
    }
}
