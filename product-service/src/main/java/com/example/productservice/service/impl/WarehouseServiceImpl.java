package com.example.productservice.service.impl;

import com.example.productservice.constant.I18nMessage;
import com.example.productservice.dto.ProductDetailCheckingDto;
import com.example.productservice.dto.ProductDetailsCheckingDto;
import com.example.productservice.entity.ProductType;
import com.example.productservice.payload.orderservice.request.ProductCheckingRequest;
import com.example.productservice.repository.ProductTypeRepository;
import com.example.productservice.repository.predicate.ProductTypePredicate;
import com.example.productservice.service.WarehouseService;
import com.example.servicefoundation.i18n.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private I18nService i18nService;

    @Override
    public ProductDetailsCheckingDto checkListProduct(List<ProductCheckingRequest> request) {
        Map<String, Integer> requestMap = request.stream()
                .collect(Collectors.toMap(
                        ProductCheckingRequest::getProductDetailId,
                        ProductCheckingRequest::getQuantity
                ));

        ProductDetailsCheckingDto response = new ProductDetailsCheckingDto();
        List<ProductDetailsCheckingDto.ProductDetailItem> productDetailItems = new ArrayList<>();

        ProductTypePredicate productTypePredicate = new ProductTypePredicate()
                .inIds(requestMap.keySet().stream().toList());
        List<ProductType> productTypes = productTypeRepository.findAll(productTypePredicate.getCriteria());
        List<String> productDetailKeys = productTypes.stream().map(ProductType::getId).toList();

        for (ProductType productType : productTypes) {
            String id = productType.getId();
            if (productType.getQuantity() < requestMap.get(id)) {
                productDetailItems.add(
                        new ProductDetailsCheckingDto.ProductDetailItem(
                                false,
                                id,
                                i18nService.getMessage(I18nMessage.ERROR_PRODUCT_QUANTITY_NOT_ENOUGH, LocaleContextHolder.getLocale())
                        )
                );
            }
        }

        for (ProductCheckingRequest product : request) {
            String id = product.getProductDetailId();
            if (!productDetailKeys.contains(id)) {
                productDetailItems.add(
                        new ProductDetailsCheckingDto.ProductDetailItem(
                                false,
                                id,
                                i18nService.getMessage(I18nMessage.ERROR_PRODUCT_DETAIL_NOT_FOUND, LocaleContextHolder.getLocale())
                        )
                );
            }
        }

        response.setStatus((productDetailItems.size() > 0) ? false : true);
        response.setProductDetailItems(productDetailItems);
        return response;
    }

    @Override
    public ProductDetailCheckingDto checkProduct(ProductCheckingRequest request) {
        ProductTypePredicate productTypePredicate = new ProductTypePredicate()
                .withProductId(request.getProductDetailId());
        Optional<ProductType> check = productTypeRepository.findOne(productTypePredicate.getCriteria());

        ProductDetailCheckingDto response = new ProductDetailCheckingDto();
        if (!check.isPresent()) {
            response.setExist(false);
            response.setInfo(I18nMessage.ERROR_PRODUCT_DETAIL_NOT_FOUND);
            return response;
        }

        ProductType productType = check.get();
        if (productType.getQuantity() < request.getQuantity()) {
            response.setExist(false);
            response.setInfo(I18nMessage.ERROR_PRODUCT_QUANTITY_NOT_ENOUGH);
            return response;
        }

        response.setExist(true);
        response.setPrice(productType.getPrice());
        productType.setQuantity(productType.getQuantity() - request.getQuantity());
        productTypeRepository.save(productType);
        return response;
    }
}
