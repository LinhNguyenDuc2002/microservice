package com.example.productservice.service.impl;

import com.example.productservice.constant.I18nMessage;
import com.example.productservice.constant.ProductStatus;
import com.example.productservice.dto.WareHouseCheckingDto;
import com.example.productservice.entity.ProductDetail;
import com.example.productservice.payload.orderservice.request.WareHouseCheckingReq;
import com.example.productservice.repository.ProductDetailRepository;
import com.example.productservice.repository.predicate.ProductDetailPredicate;
import com.example.productservice.service.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Override
    public WareHouseCheckingDto checkWarehouse(List<WareHouseCheckingReq> request) {
        Map<String, Integer> requestMap = request.stream()
                .collect(Collectors.toMap(
                        WareHouseCheckingReq::getProductDetailId,
                        WareHouseCheckingReq::getQuantity
                ));

        WareHouseCheckingDto response = new WareHouseCheckingDto();
        List<WareHouseCheckingDto.ProductDetailItem> productDetailItems = new ArrayList<>();

        ProductDetailPredicate productDetailPredicate = new ProductDetailPredicate()
                .withStatus(true) // selling
                .inIds((List<String>) requestMap.keySet());
        List<ProductDetail> productDetails = productDetailRepository.findAll(productDetailPredicate.getCriteria());
        List<String> productDetailKey = productDetails.stream().map(ProductDetail::getId).toList();

        if(productDetails.size() != requestMap.size()) {
            request.stream().forEach(item -> {
                WareHouseCheckingDto.ProductDetailItem productDetailItem = new WareHouseCheckingDto.ProductDetailItem();
                if(!productDetailKey.contains(item.getProductDetailId())) {
                    productDetailItem.setExist(false);
                    productDetailItem.setProductDetailId(item.getProductDetailId());
                }
                productDetailItems.add(productDetailItem);
            });

            response.setStatus(ProductStatus.PRODUCT_NOT_FOUND.name());
            response.setProductDetailItems(productDetailItems);
            return response;
        }
        else {
            response.setStatus(ProductStatus.SUCCESS.name());
            for (ProductDetail productDetail : productDetails) {
                Integer quantity = requestMap.get(productDetail.getId());
                WareHouseCheckingDto.ProductDetailItem item = new WareHouseCheckingDto.ProductDetailItem();

                if (productDetail.getQuantity() >= quantity) {
                    item.setExist(true);
                    productDetail.setQuantity(productDetail.getQuantity() - quantity);
                }
                else {
                    item.setExist(false);
                    response.setStatus(ProductStatus.OUT_OF_STOCK.name());
                }

                item.setProductDetailId(productDetail.getId());
                productDetailItems.add(item);
            }

            if(response.getStatus().equals(ProductStatus.SUCCESS.name())) {
                productDetailRepository.saveAll(productDetails);
            }
            response.setProductDetailItems(productDetailItems);
            return response;
        }
    }
}
