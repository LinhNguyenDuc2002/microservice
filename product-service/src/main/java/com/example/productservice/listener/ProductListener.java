package com.example.productservice.listener;

import com.example.productservice.cache.ProductCacheManager;
import com.example.productservice.entity.Product;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductListener {
    @Autowired
    private ProductCacheManager productCacheManager;

    @PreUpdate
    public void preUpate(Product product) {

    }

    @PostUpdate
    public void postUpdate(Product product) {
        log.info("Post update");
        productCacheManager.clear();
    }
}
