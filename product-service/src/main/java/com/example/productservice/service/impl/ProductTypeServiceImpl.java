package com.example.productservice.service.impl;

import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.dto.ProductTypeDTO;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.ProductType;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.ProductTypeMapper;
import com.example.productservice.payload.ProductTypeRequest;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.ProductTypeRepository;
import com.example.productservice.service.ProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ProductTypeServiceImpl implements ProductTypeService {
    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Override
    public ProductTypeDTO add(String id, ProductTypeRequest productTypeRequest) throws NotFoundException, InvalidException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
                });

        if(!StringUtils.hasText(productTypeRequest.getName()) ||
                productTypeRequest.getQuantity() == null ||
                productTypeRequest.getPrice() == null) {
            throw new InvalidException((""));
        }

        if(product.getProductTypes() == null || product.getProductTypes().isEmpty()) {
            product.setQuantity(0L);
            product.setProductTypes(new ArrayList<>());
        }

        ProductType productType = mapToProductType(productTypeRequest);
        product.setQuantity(product.getQuantity() + productType.getQuantity());
        product.setPrice(Math.min(product.getPrice(), productType.getPrice()));
        productRepository.save(product);

        productType.setProduct(product);
        productTypeRepository.save(productType);

        return productTypeMapper.toDto(productType);
    }

    @Override
    public List<ProductTypeDTO> getAll(String id) throws NotFoundException, InvalidException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
                });

        return productTypeMapper.toDtoList((List<ProductType>) product.getProductTypes());
    }

    @Override
    public ProductTypeDTO get(String id) throws NotFoundException, InvalidException {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException("");
                });

        return productTypeMapper.toDto(productType);
    }

    @Override
    public ProductTypeDTO update(String id, ProductTypeRequest productTypeRequest) throws NotFoundException, InvalidException {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException("");
                });

        if(!StringUtils.hasText(productTypeRequest.getName()) ||
                productTypeRequest.getQuantity() == null ||
                productTypeRequest.getPrice() == null) {
            throw new InvalidException((""));
        }

        productType.setName(productTypeRequest.getName());
        productType.setPrice(productTypeRequest.getPrice());
        productType.setDescription(productTypeRequest.getDescription());

        Product product = productType.getProduct();
        Long quantity = product.getQuantity() - productType.getQuantity();
        productType.setQuantity(productTypeRequest.getQuantity());
        product.setQuantity(quantity + productType.getQuantity());
        product.setPrice(Math.min(product.getPrice(), productType.getPrice()));

        productRepository.save(product);
        productTypeRepository.save(productType);

        return productTypeMapper.toDto(productType);
    }

    @Override
    public void delete(String id) throws NotFoundException, InvalidException {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException("");
                });

        Product product = productType.getProduct();
        product.setQuantity(product.getQuantity() - productType.getQuantity());
        product.setPrice(
                Collections.min(product.getProductTypes().stream().map(ProductType::getPrice).toList())
        );

        productTypeRepository.deleteById(id);
        productRepository.save(product);
    }

    private ProductType mapToProductType(ProductTypeRequest productTypeRequest) {
        return ProductType.builder()
                .name(productTypeRequest.getName())
                .quantity(productTypeRequest.getQuantity())
                .price(productTypeRequest.getPrice())
                .description(productTypeRequest.getDescription())
                .build();
    }
}
