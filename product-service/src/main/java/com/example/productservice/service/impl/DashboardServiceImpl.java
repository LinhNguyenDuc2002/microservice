package com.example.productservice.service.impl;

import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.dto.statistic.DetailProductStatisticsDTO;
import com.example.productservice.dto.statistic.ProductStatisticsDTO;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.Shop;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.repository.DetailRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.ShopRepository;
import com.example.productservice.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DetailRepository detailRepository;

    @Override
    public ProductStatisticsDTO productStatistics(String id) throws NotFoundException {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_SHOP_NOT_FOUND);
                });

        ProductStatisticsDTO productStatisticsDTO = new ProductStatisticsDTO();
        productStatisticsDTO.setPurchasedProduct(Long.valueOf(shop.getProducts().size()));
        productStatisticsDTO.setProducts(new ArrayList<>());

        for(Product product : shop.getProducts()) {
            DetailProductStatisticsDTO detailProduct = DetailProductStatisticsDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .quantity(product.getQuantity())
                    .sold(product.getSold())
                    .sales(detailRepository.getSales(product.getId()))
                    .build();

            productStatisticsDTO.getProducts().add(detailProduct);
        }

        return productStatisticsDTO;
    }
}
