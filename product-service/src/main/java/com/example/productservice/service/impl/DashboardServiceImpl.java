package com.example.productservice.service.impl;

import com.example.productservice.dto.statistic.ProductStatisticsDTO;
import com.example.productservice.entity.Product;
import com.example.productservice.payload.response.PageResponse;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.ShopRepository;
import com.example.productservice.repository.predicate.ProductPredicate;
import com.example.productservice.service.DashboardService;
import com.example.productservice.service.OrderService;
import com.example.productservice.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @Override
    public PageResponse<ProductStatisticsDTO> productStatistics(Integer page, Integer size, String shopId, String categoryId) throws Exception {
        Pageable pageable = PageUtil.getPage(page, size);

        ProductPredicate productPredicate = new ProductPredicate()
                .shop(shopId)
                .withCategoryId(categoryId);
        Page<Product> products = productRepository.findAll(productPredicate.getCriteria(), pageable);

        List<String> idProductList = products.getContent().stream().map(Product::getId).toList();
        Map<String, Double> sales = orderService.calculateSales(idProductList);

        List<ProductStatisticsDTO> productStatisticsDTOS = new ArrayList<>();
        for(Product product : products.getContent()) {
            String id = product.getId();
            productStatisticsDTOS.add(
                    ProductStatisticsDTO.builder()
                            .id(id)
                            .name(product.getName())
                            .currentPrice(product.getPrice())
                            .available(product.getQuantity())
                            .sold(product.getSold())
                            .sales(sales.get(id))
                            .build()
            );
        }

        PageResponse pageResponse = PageResponse.<ProductStatisticsDTO>builder()
                .index(page)
                .totalPage(products.getTotalPages())
                .elements(productStatisticsDTOS)
                .build();

        return pageResponse;
    }
}
