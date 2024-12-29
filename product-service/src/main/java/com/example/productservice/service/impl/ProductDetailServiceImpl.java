package com.example.productservice.service.impl;

import com.cloudinary.utils.ObjectUtils;
import com.example.productservice.constant.CloudinaryConstant;
import com.example.productservice.constant.I18nMessage;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.ProductDetailCheckingDto;
import com.example.productservice.dto.ProductDetailDTO;
import com.example.productservice.dto.request.ProductDetailRequest;
import com.example.productservice.entity.Image;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.ProductDetail;
import com.example.productservice.exception.InvalidationException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.i18n.I18nService;
import com.example.productservice.mapper.ProductDetailMapper;
import com.example.productservice.payload.orderservice.request.UpdateOrderDetailReq;
import com.example.productservice.repository.ImageRepository;
import com.example.productservice.repository.ProductDetailRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.predicate.ProductDetailPredicate;
import com.example.productservice.service.CloudinaryService;
import com.example.productservice.service.ProductDetailService;
import com.example.productservice.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductDetailServiceImpl implements ProductDetailService {
    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailMapper productDetailMapper;

    @Autowired
    private KafkaOrderService kafkaOrderService;

    @Autowired
    private I18nService i18nService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public ProductDetailDTO add(String id, ProductDetailRequest productDetailRequest) throws NotFoundException, InvalidationException, IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_PRODUCT_NOT_FOUND);
                });

        String imageId = UUID.randomUUID().toString();
        ProductDetail productDetail = ProductDetail.builder()
                .name(productDetailRequest.getName())
                .description(productDetailRequest.getDescription())
                .quantity(productDetailRequest.getQuantity())
                .price(productDetailRequest.getPrice())
                .sold(0L)
                .product(product)
                .imageId(imageId)
                .build();

        Map<String, String> args = new HashMap<>();
        args.put(CloudinaryConstant.FOLDER, "product-service");
        args.put(CloudinaryConstant.PUBLIC_ID, imageId);
        cloudinaryService.upload(productDetailRequest.getImage(), args);
        productDetailRepository.save(productDetail);

        return productDetailMapper.toDto(productDetail);
    }

    @Override
    public PageDTO<ProductDetailDTO> getAll(String id, Integer page, Integer size, String search, List<String> sortColumns) throws NotFoundException, InvalidationException {
        boolean check = productRepository.existsById(id);
        if(!check) {
            throw new NotFoundException(I18nMessage.ERROR_PRODUCT_NOT_FOUND);
        }

        Pageable pageable = (sortColumns == null) ? PageUtil.getPage(page, size) : PageUtil.getPage(page, size, sortColumns.toArray(new String[0]));
        ProductDetailPredicate productDetailPredicate = new ProductDetailPredicate()
                .withProductId(id)
                .search(search);
        Page<ProductDetail> productDetails = productDetailRepository.findAll(productDetailPredicate.getCriteria(), pageable);

        List<ProductDetailDTO> productDetailDTOS = new ArrayList<>();
        productDetails.getContent().parallelStream()
                .forEach(productDetail -> {
                    ProductDetailDTO productDetailDTO = productDetailMapper.toDto(productDetail);

                    Optional<Image> image = imageRepository.findById(productDetail.getId());
                    if (image.isPresent()) {
                        productDetailDTO.setImageUrl(image.get().getSecureUrl());
                    }
                    productDetailDTOS.add(productDetailDTO);
                });

        PageDTO pageDTO = PageDTO.<ProductDetailDTO>builder()
                .index(productDetails.getNumber())
                .totalPage(productDetails.getTotalPages())
                .elements(productDetailDTOS)
                .build();

        return pageDTO;
    }

    @Override
    public ProductDetailDTO get(String id) throws NotFoundException, InvalidationException {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_PRODUCT_DETAIL_NOT_FOUND);
                });

        ProductDetailDTO productDetailDTO = productDetailMapper.toDto(productDetail);

        Optional<Image> image = imageRepository.findById(productDetail.getId());
        if (image.isPresent()) {
            productDetailDTO.setImageUrl(image.get().getSecureUrl());
        }

        return productDetailMapper.toDto(productDetail);
    }

    @Override
    public ProductDetailDTO update(String id, ProductDetailRequest productDetailRequest) throws NotFoundException, InvalidationException, IOException {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_PRODUCT_DETAIL_NOT_FOUND);
                });

        if (productDetailRequest.getImage() != null) {
            String imageId = productDetail.getImageId();

            cloudinaryService.destroy(imageId);

            Map<String, String> args = ObjectUtils.asMap();
            args.put(CloudinaryConstant.FOLDER, "product-service");
            args.put(CloudinaryConstant.PUBLIC_ID, imageId);
            cloudinaryService.upload(productDetailRequest.getImage(), args);
        }

        productDetail.setName(productDetailRequest.getName());
        productDetail.setDescription(productDetailRequest.getDescription());

        if (productDetail.getPrice() != productDetailRequest.getPrice() ||
                productDetail.getQuantity() != productDetailRequest.getQuantity()) {
            productDetail.setQuantity(productDetailRequest.getQuantity());
            productDetail.setPrice(productDetailRequest.getPrice());

            UpdateOrderDetailReq updateOrderDetailReq = UpdateOrderDetailReq.builder()
                    .productDetailId(id)
                    .price(productDetailRequest.getPrice())
                    .quantity(productDetailRequest.getQuantity())
                    .build();
            kafkaOrderService.updateOrderDetail(updateOrderDetailReq);
        }
        productDetailRepository.save(productDetail);

        return productDetailMapper.toDto(productDetail);
    }

    @Override
    public void delete(String id) throws NotFoundException, InvalidationException {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_PRODUCT_DETAIL_NOT_FOUND);
                });

        Product product = productDetail.getProduct();
        if (product.getProductDetails().size() == 1) {
            throw new InvalidationException(I18nMessage.ERROR_PRODUCT_DETAIL_ONLY_ONE);
        }

        productDetail.setStatus(false);
        productDetailRepository.save(productDetail);
    }

    @Override
    public ProductDetailCheckingDto checkExist(String id, Integer quantity) {
        ProductDetailCheckingDto response = new ProductDetailCheckingDto();

        Optional<ProductDetail> check = productDetailRepository.findById(id);
        if (check.isPresent() && check.get().getQuantity() >= quantity) {
            response.setExist(true);
            response.setPrice(check.get().getPrice());
            return response;
        }

        response.setExist(false);
        response.setInfo(
                check.isPresent() ? I18nMessage.WARN_PRODUCT_DETAIL_SOLD_OUT : I18nMessage.ERROR_PRODUCT_DETAIL_NOT_FOUND
        );
        return response;
    }
}
