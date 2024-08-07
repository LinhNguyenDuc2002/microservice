package com.example.productservice.service.impl;

import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.ProductType;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.orderservice.request.WareHouseCheckingReq;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.ProductTypeRepository;
import com.example.productservice.repository.predicate.ProductPredicate;
import com.example.productservice.repository.predicate.ProductTypePredicate;
import com.example.productservice.service.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    private Lock lock = new ReentrantLock();

    @Override
    public Map<String, List<String>> checkWarehouse(List<WareHouseCheckingReq> request) throws InvalidException, NotFoundException {
        List<String> productKey = request.stream().map(WareHouseCheckingReq::getProductId).toList();
        Map<String, Product> products = new HashMap<>();
        productKey.stream().forEach(key -> {
            try {
                Product product = productRepository.findById(key)
                        .orElseThrow(() -> {
                            return new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
                        });
                products.put(key, product);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        Map<String, List<String>> response = new LinkedHashMap<>();
        Map<String, ProductType> productTypes = new HashMap<>();
        for (WareHouseCheckingReq wareHouseCheckingReq : request) {
            Product product = products.get(wareHouseCheckingReq.getProductId());
            Long quantity = product.getQuantity();

            if (StringUtils.hasText(wareHouseCheckingReq.getProductTypeId())) {
                ProductTypePredicate productTypePredicate = new ProductTypePredicate()
                        .withId(wareHouseCheckingReq.getProductTypeId())
                        .withProductId(product.getId());
                ProductType productType = productTypeRepository.findOne(productTypePredicate.getCriteria())
                        .orElseThrow(() -> {
                            return new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
                        });
                quantity = productType.getQuantity();
                productTypes.put(productType.getId(), productType);
            }

            if (wareHouseCheckingReq.getQuantity() > quantity) {
                throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_SOLD_OUT);
            }
        }

        for (WareHouseCheckingReq wareHouseCheckingReq : request) {
            Product product = products.get(wareHouseCheckingReq.getProductId());
            Integer quantity = wareHouseCheckingReq.getQuantity();

            if (StringUtils.hasText(wareHouseCheckingReq.getProductTypeId())) {
                ProductType productType = productTypes.get(wareHouseCheckingReq.getProductTypeId());
                productType.setQuantity(productType.getQuantity() - quantity);
            }

            product.setQuantity(product.getQuantity() - quantity);
            product.setSold(product.getSold() + quantity);

            String shopId = product.getShop().getId();
            if (!response.containsKey(shopId)) {
                response.put(shopId, new ArrayList<>());
            }
            response.get(shopId).add(product.getId());
        }

        productRepository.saveAll(products.values());
        productTypeRepository.saveAll(productTypes.values());

        return response;
    }

    @Override
    public Map<String, List<String>> groupDetails(List<String> productKey) {
        ProductPredicate productPredicate = new ProductPredicate().inProductIds(productKey);
        List<Product> products = productRepository.findAll(productPredicate.getCriteria());

        Map<String, List<String>> response = new LinkedHashMap<>();
        for (Product product : products) {
            String shopId = product.getShop().getId();
            if (!response.containsKey(shopId)) {
                response.put(shopId, new ArrayList<>());
            }
            response.get(shopId).add(product.getId());
        }

        return response;
    }
}
