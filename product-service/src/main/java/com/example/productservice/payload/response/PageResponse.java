package com.example.productservice.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public class PageResponse<T> {
    @JsonProperty("page_number")
    private Integer index;

    @JsonProperty("total_page")
    private Integer totalPage;

    @JsonProperty("elements")
    private List<T> elements;
}
