package com.example.productservice.service;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.request.ProductFormRequest;
import com.example.servicefoundation.exception.I18nException;

import java.io.IOException;

public interface ProductService {
    ProductDTO add(ProductFormRequest productFormRequest) throws IOException, I18nException;
}
