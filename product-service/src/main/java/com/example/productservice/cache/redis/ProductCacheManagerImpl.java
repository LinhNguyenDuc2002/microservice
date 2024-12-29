package com.example.productservice.cache.redis;

import com.example.productservice.cache.ProductCacheManager;
import com.example.productservice.constant.CacheConstant;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.ProductDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class ProductCacheManagerImpl implements ProductCacheManager {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void clear() {
        // Lấy tất cả key cache của service
        Set<String> keys = redisTemplate.keys(CacheConstant.PREFIX_CACHE + ":*");
        redisTemplate.delete(new ArrayList<>(keys));
    }

    @Override
    public void saveAllProducts(PageDTO<ProductDTO> products, Integer page, Integer size, String category) throws JsonProcessingException {
        String key = String.format(CacheConstant.KEY_PRODUCT_CACHE, page, size, category);
        String json = objectMapper.writeValueAsString(products);
        redisTemplate.opsForValue().set(key, json);
    }

    @Override
    public PageDTO<ProductDTO> getAllProducts(Integer page, Integer size, String category) throws JsonProcessingException {
        String key = String.format(CacheConstant.KEY_PRODUCT_CACHE, page, size, category);
        String json = (String) redisTemplate.opsForValue().get(key);

        PageDTO<ProductDTO> products = (json != null) ? objectMapper.readValue(json, new TypeReference<PageDTO<ProductDTO>>() {
        }) : null;
        return products;
    }
}
