package com.example.productservice.service.impl;

import com.example.productservice.dto.statistic.ProductStatisticsDTO;
import com.example.productservice.entity.Product;
import com.example.productservice.dto.PageDTO;
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
    public PageDTO<ProductStatisticsDTO> productStatistics(Integer page, Integer size, String shopId, String categoryId, List<String> sortColumns) throws Exception {
        Pageable pageable = (sortColumns == null) ? PageUtil.getPage(page, size) : PageUtil.getPage(page, size, sortColumns.toArray(new String[0]));

        ProductPredicate productPredicate = new ProductPredicate()
                .withShopId(shopId)
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

        PageDTO pageDTO = PageDTO.<ProductStatisticsDTO>builder()
                .index(products.getNumber())
                .totalPage(products.getTotalPages())
                .elements(productStatisticsDTOS)
                .build();

        return pageDTO;
    }
}
