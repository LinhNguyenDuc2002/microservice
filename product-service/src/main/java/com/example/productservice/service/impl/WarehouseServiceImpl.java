package com.example.productservice.service.impl;

import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.predicate.ProductPredicate;
import com.example.productservice.service.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    private Lock lock = new ReentrantLock();

    @Override
    public Map<String, List<String>> checkWarehouse(Map<String, Integer> productList) throws InvalidException {
        List<String> productKey = productList.keySet().stream().toList();

        ProductPredicate productPredicate = new ProductPredicate().withIds(productKey);
        List<Product> products = productRepository.findAll(productPredicate.getCriteria());

        for(Product product : products) {
            if (productList.get(product.getId()) > product.getQuantity()) {
                throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_SOLD_OUT);
            }
        }

        for(Product product : products) {
            product.setQuantity(product.getQuantity() - productList.get(product.getId()));
            product.setSold(product.getSold() + productList.get(product.getId()));
        }

        return groupDetails(productKey);
    }

    @Override
    public Map<String, List<String>> groupDetails(List<String> productKey) {
        ProductPredicate productPredicate = new ProductPredicate().withIds(productKey);
        List<Product> products = productRepository.findAll(productPredicate.getCriteria());

        Map<String, List<String>> response = new LinkedHashMap<>();
        for(Product product : products) {
            String shopId = product.getShop().getId();
            if(!response.containsKey(shopId)) {
                response.put(shopId, new ArrayList<>());
            }
            response.get(shopId).add(product.getId());
        }

        return response;
    }
}
