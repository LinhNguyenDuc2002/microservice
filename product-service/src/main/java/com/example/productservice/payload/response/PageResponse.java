package com.example.productservice.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    @JsonProperty("page_number")
    private Integer index;

    @JsonProperty("total_page")
    private Integer totalPage;

    @JsonProperty("elements")
    private List<T> elements;
}
