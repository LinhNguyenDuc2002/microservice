package com.example.productservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageDTO<T> {
    @JsonProperty("page_number")
    private Integer index;

    @JsonProperty("total_page")
    private Integer totalPage;

    @JsonProperty("elements")
    private List<T> elements;
}
