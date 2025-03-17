package com.example.productservice.service.impl;

import com.example.productservice.constant.I18nMessage;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.request.ProductFormRequest;
import com.example.productservice.dto.request.ProductRequest;
import com.example.productservice.dto.request.TypeRequest;
import com.example.productservice.entity.Feature;
import com.example.productservice.entity.Product;
import com.example.productservice.mybatis.mapper.CategoryMapper;
import com.example.productservice.mybatis.mapper.ProductMapper;
import com.example.productservice.service.CloudinaryService;
import com.example.productservice.service.ProductService;
import com.example.servicefoundation.exception.I18nException;
import com.example.servicefoundation.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final String FOLDER = "product-service";
    private final Integer MAX_IMAGE_NUMBER = 4;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public ProductDTO add(ProductFormRequest productFormRequest) throws IOException, I18nException {
//        Optional<String> userId = SecurityUtils.getLoggedInUserId();

        ProductRequest productRequest = productFormRequest.getProduct();
        boolean checkCategory = categoryMapper.existById(productRequest.getCategoryId());
        if (!checkCategory) {
            throw I18nException.builder()
                    .code(HttpStatus.NOT_FOUND)
                    .message(I18nMessage.ERROR_CATEGORY_NOT_FOUND)
                    .build();
        }

        Map<String, MultipartFile> images = new HashMap<>();
        long totalSize = 0;
        for (MultipartFile file : productRequest.getImages()) {
            if ((file.getSize() / (1024 * 1024)) > 2) {
                throw new ValidateException(I18nMessage.ERROR_PRODUCT_DETAIL_NAME_DUPLICATED);
            }
            totalSize += file.getSize();
            images.put(UUID.randomUUID().toString(), file);
        }
        double totalSizeInMB = totalSize / (1024 * 1024);
        if (totalSizeInMB > 4) {
            throw new ValidateException(I18nMessage.ERROR_PRODUCT_DETAIL_NAME_DUPLICATED);
        }

        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .status(true)
                .sold(0)
                .categoryId(productRequest.getCategoryId())
                .imageIds(StringUtil.joinDelimiter(images.keySet().stream().toList()))
                .build();
        if (productFormRequest.getTypes().isEmpty()) {
            product.setPrice(productRequest.getPrice());
            product.setQuantity(productRequest.getQuantity());
            productMapper.insert(product);
            cloudinaryService.upload(images);
            return null;
        }

        List<Feature> features = new ArrayList<>();
        for (TypeRequest typeRequest : productFormRequest.getTypes()) {
            Feature feature = Feature.builder()
                    .id(UUID.randomUUID().toString())
                    .name(typeRequest.getName())
                    .build();

            features.add(feature);
        }

        return null;
    }
}
