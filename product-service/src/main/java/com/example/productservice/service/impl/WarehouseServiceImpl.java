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

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Boolean checkWarehouse(Map<String, Integer> productList) throws InvalidException {
        Set<String> productKey = productList.keySet();

        ProductPredicate productPredicate = new ProductPredicate().withIds(productKey.stream().toList());
        List<Product> products = productRepository.findAll(productPredicate.getCriteria());


        for(Product product : products) {
            if (productList.get(product.getId()) > product.getQuantity()) {
                throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_SOLD_OUT);
            }
        }

        for(Product product : products) {
            product.setQuantity(product.getQuantity() - productList.get(product.getId()));
        }
        productRepository.saveAll(products);

        return true;
    }
}
