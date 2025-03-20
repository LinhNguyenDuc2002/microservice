package com.example.productservice.service.impl;

import com.example.productservice.cache.ProductCacheManager;
import com.example.productservice.constant.I18nMessage;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.ProductDetailDTO;
import com.example.productservice.dto.ProductTypeDTO;
import com.example.productservice.dto.request.BasicProductRequest;
import com.example.productservice.dto.request.ProductFormRequest;
import com.example.productservice.dto.request.ProductRequest;
import com.example.productservice.dto.request.ProductTypeRequest;
import com.example.productservice.dto.request.SubTypeRequest;
import com.example.productservice.dto.request.TypeRequest;
import com.example.productservice.entity.Attribute;
import com.example.productservice.entity.Category;
import com.example.productservice.entity.Feature;
import com.example.productservice.entity.Image;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.ProductAttribute;
import com.example.productservice.entity.ProductType;
import com.example.productservice.mapper.FeatureMapper;
import com.example.productservice.mapper.ProductDetailMapper;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.repository.AttributeRepository;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.repository.FeatureRepository;
import com.example.productservice.repository.ImageRepository;
import com.example.productservice.repository.ProductAttributeRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.ProductTypeRepository;
import com.example.productservice.repository.ShopRepository;
import com.example.productservice.repository.predicate.ProductPredicate;
import com.example.productservice.service.CloudinaryService;
import com.example.productservice.service.ProductService;
import com.example.servicefoundation.exception.I18nException;
import com.example.servicefoundation.util.PaginationUtil;
import com.example.servicefoundation.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final String FOLDER = "product-service";
    private final Integer MAX_IMAGE_NUMBER = 4;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductDetailMapper productDetailMapper;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private FeatureMapper featureMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductCacheManager productCacheManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaOrderService orderService;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Transactional
    @Override
    public ProductDTO add(ProductFormRequest productFormRequest) throws IOException, I18nException {
//        Optional<String> userId = SecurityUtils.getLoggedInUserId();

        ProductRequest productRequest = productFormRequest.getProduct();
        List<TypeRequest> types = productFormRequest.getTypes();
        boolean check = productRepository.existsByName(productRequest.getName());
        if (check) {
            throw I18nException.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(I18nMessage.ERROR_PRODUCT_NAME_EXISTED)
                    .build();
        }

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_CATEGORY_NOT_FOUND)
                            .build();
                });

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
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .category(category)
                .status(true)
                .sold(0)
                .imageIds(StringUtil.joinDelimiter(images.keySet().stream().toList()))
                .build();

        // No product type
        if (types.isEmpty()) {
            product.setPrice(productRequest.getPrice());
            product.setQuantity(product.getQuantity());
            cloudinaryService.upload(images);
            productRepository.save(product);
            return productMapper.toDto(product);
        }

        // There are product types
        List<Feature> features = new ArrayList<>();
        for (TypeRequest type : types) {
            Feature feature = featureRepository.findById(type.getName())
                    .orElseGet(() -> Feature.builder()
                            .name(type.getName())
                            .attributes(new ArrayList<>())
                            .build()
                    );
            features.add(feature);
        }

        product.setProductTypes(new ArrayList<>());
        List<ProductTypeRequest> productTypeRequests = productRequest.getProductTypes();
        if (features.size() == 1 || productTypeRequests.get(0).getTypes().isEmpty()) {
            for (ProductTypeRequest productTypeRequest : productTypeRequests) {
                MultipartFile file = productTypeRequest.getImage();
                if ((file.getSize() / (1024 * 1024)) > 2) {
                    throw new ValidateException(I18nMessage.ERROR_PRODUCT_DETAIL_NAME_DUPLICATED);
                }

                String id = UUID.randomUUID().toString();
                images.put(id, file);
                ProductType productType = ProductType.builder()
                        .price(productTypeRequest.getPrice())
                        .quantity(productTypeRequest.getQuantity())
                        .imageId(id)
                        .product(product)
                        .productAttributes(new ArrayList<>())
                        .build();

                List<Attribute> attributes = attributeRepository.findByIdAndFeatureId(productTypeRequest.getName(), features.get(0).getId());
                Attribute attribute = null;
                if (attributes == null) {
                    attribute = Attribute.builder()
                            .value(productTypeRequest.getName())
                            .feature(features.get(0))
                            .build();
                    features.get(0).getAttributes().add(attribute);
                } else {
                    attribute = attributes.get(0);
                }

                ProductAttribute productAttribute = ProductAttribute.builder()
                        .id(
                                new ProductAttribute.ProductAttributeId(productType.getId(), attribute.getId())
                        )
                        .productType(productType)
                        .attribute(attribute)
                        .level(1)
                        .build();
                productType.getProductAttributes().add(productAttribute);
                product.getProductTypes().add(productType);
            }

            cloudinaryService.upload(images);
            featureRepository.saveAll(features);
            productRepository.save(product);
            return productMapper.toDto(product);
        }

        for (ProductTypeRequest productTypeRequest : productTypeRequests) {
            MultipartFile file = productTypeRequest.getImage();
            if ((file.getSize() / (1024 * 1024)) > 2) {
                throw new ValidateException(I18nMessage.ERROR_PRODUCT_DETAIL_NAME_DUPLICATED);
            }

            String id = UUID.randomUUID().toString();
            images.put(id, file);

            ProductType productType = ProductType.builder()
                    .product(product)
                    .imageId(id)
                    .productAttributes(new ArrayList<>())
                    .build();

            List<Attribute> attributes = attributeRepository.findByIdAndFeatureId(productTypeRequest.getName(), features.get(0).getId());
            Attribute attribute = null;
            if (attributes.isEmpty()) {
                attribute = Attribute.builder()
                        .value(productTypeRequest.getName())
                        .feature(features.get(0))
                        .build();
                features.get(0).getAttributes().add(attribute);
            } else {
                attribute = attributes.get(0);
            }

            ProductAttribute productAttribute = ProductAttribute.builder()
                    .id(
                            new ProductAttribute.ProductAttributeId(productType.getId(), attribute.getId())
                    )
                    .productType(productType)
                    .attribute(attribute)
                    .level(1)
                    .build();
            productType.getProductAttributes().add(productAttribute);

            List<SubTypeRequest> subTypeRequests = productTypeRequest.getTypes();
            for (SubTypeRequest subTypeRequest : subTypeRequests) {
                productType.setQuantity(subTypeRequest.getQuantity());
                productType.setPrice(subTypeRequest.getPrice());

                List<Attribute> subAttributes = attributeRepository.findByIdAndFeatureId(productTypeRequest.getName(), features.get(1).getId());
                Attribute subAttribute = null;
                if (subAttributes.isEmpty()) {
                    subAttribute = Attribute.builder()
                            .value(subTypeRequest.getName())
                            .feature(features.get(1))
                            .build();
                    features.get(1).getAttributes().add(subAttribute);
                } else {
                    subAttribute = subAttributes.get(0);
                }

                ProductAttribute subProductAttribute = ProductAttribute.builder()
                        .id(
                                new ProductAttribute.ProductAttributeId(productType.getId(), subAttribute.getId())
                        )
                        .productType(productType)
                        .attribute(subAttribute)
                        .level(2)
                        .build();
                productType.getProductAttributes().add(subProductAttribute);
            }
            product.getProductTypes().add(productType);
        }

        cloudinaryService.upload(images);
        featureRepository.saveAll(features);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public ProductDTO update(String id, BasicProductRequest productRequest) throws IOException, I18nException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_PRODUCT_NOT_FOUND)
                            .build();
                });
        if (!productRequest.getName().equals(product.getName())) {
            boolean check = productRepository.existsByName(productRequest.getName());
            if (check) {
                throw I18nException.builder()
                        .code(HttpStatus.BAD_REQUEST)
                        .message(I18nMessage.ERROR_PRODUCT_NAME_EXISTED)
                        .build();
            }
        }

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_CATEGORY_NOT_FOUND)
                            .build();
                });

        if (productRequest.getImages() != null && !productRequest.getImages().isEmpty()) {
            Map<String, MultipartFile> images = new HashMap<>();
            long totalSize = 0;
            for (MultipartFile file : productRequest.getImages()) {
                totalSize += file.getSize();
                images.put(UUID.randomUUID().toString(), file);
            }
            double totalSizeInMB = totalSize / (1024 * 1024);

            if (productRequest.getImages().size() > MAX_IMAGE_NUMBER || totalSizeInMB > 4) {
                throw new ValidateException(I18nMessage.ERROR_IMAGE_NUMBER_EXCEEDED);
            }

            List<String> ids = StringUtil.splitDelimiter(product.getImageIds());
            cloudinaryService.destroy(ids);
            imageRepository.deleteAllById(ids);
            product.setImageIds(StringUtil.joinDelimiter(images.keySet().stream().toList()));
            cloudinaryService.upload(images);
        }

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(category);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    @Override
    public PageDTO<ProductDTO> getAll(Integer page, Integer size, String search, String category, List<String> sortColumns) throws JsonProcessingException {
//        PageDTO<ProductDTO> productCache = productCacheManager.getAllProducts(page, size, category);
//        if (productCache != null) return productCache;

        Pageable pageable = (sortColumns == null) ? PaginationUtil.getPage(page, size) : PaginationUtil.getPage(page, size, sortColumns.toArray(new String[0]));

        ProductPredicate productPredicate = new ProductPredicate()
                .withCategoryId(category)
                .search(search);
        Page<Product> products = productRepository.findAll(productPredicate.getCriteria(), pageable);

        List<ProductDTO> productDTOS = products.getContent().stream()
                .map(product -> {
                    ProductDTO productDTO = productMapper.toDto(product);

                    List<Image> image = imageRepository.findAllById(StringUtil.splitDelimiter(product.getImageIds()));
                    productDTO.setImageUrls(image.stream().map(Image::getSecureUrl).toList());

                    Integer quantity = 0;
                    Double maxPrice = 0.0;
                    for (ProductType productType : product.getProductTypes()) {
                        quantity += productType.getQuantity();
                        Double price = productType.getPrice();
                        if (price > maxPrice) {
                            maxPrice = price;
                        }
                    }

                    Double minPrice = product.getProductTypes().stream()
                            .map(ProductType::getPrice)
                            .filter(price -> price != null)
                            .min(Double::compareTo)
                            .orElse(0.0);

                    productDTO.setQuantity(quantity);
                    productDTO.setMaxPrice(maxPrice);
                    productDTO.setMinPrice(minPrice);
                    return productDTO;
                })
                .collect(Collectors.toList());

        PageDTO pageDTO = PageDTO.<ProductDTO>builder()
                .index(products.getNumber())
                .totalPage(products.getTotalPages())
                .elements(productDTOS)
                .build();
//        productCacheManager.saveAllProducts(pageDTO, page, size, category);
        return pageDTO;
    }

    @Override
    public ProductDetailDTO get(String id) throws I18nException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_PRODUCT_NOT_FOUND)
                            .build();
                });

        if (product.getProductTypes().isEmpty()) {
            List<Image> images = imageRepository.findAllById(StringUtil.splitDelimiter(product.getImageIds()));
            return ProductDetailDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .quantity(product.getQuantity())
                    .sold(product.getSold())
                    .price(product.getPrice())
                    .imageUrls(images.stream().map(Image::getSecureUrl).toList())
                    .build();
        }

        List<Image> images = imageRepository.findAllById(StringUtil.splitDelimiter(product.getImageIds()));
        Integer quantity = 0;
        ProductDetailDTO productDetail = ProductDetailDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .sold(product.getSold())
                .imageUrls(images.stream().map(Image::getSecureUrl).toList())
                .productTypes(new ArrayList<>())
                .features(new ArrayList<>())
                .build();

        for (ProductType productType : product.getProductTypes()) {
            Image image = imageRepository.findById(productType.getImageId()).get();
            quantity += productType.getQuantity();
            ProductTypeDTO productTypeDTO = ProductTypeDTO.builder()
                    .id(product.getId())
                    .imageUrl(image.getSecureUrl())
                    .types(new ArrayList<>())
                    .build();

            List<ProductAttribute> productAttributes = (List<ProductAttribute>) productType.getProductAttributes();
            int length = productAttributes.size();
            if (length == 1) {
                productTypeDTO.setName(productAttributes.get(0).getAttribute().getValue());
                productTypeDTO.setQuantity(productType.getQuantity());
                productTypeDTO.setPrice(productType.getPrice());
            } else {
                int checkFeature = 0;
                for (ProductAttribute productAttribute : productAttributes) {
                    int level = productAttribute.getLevel();
                    if (checkFeature < level) {
                        productDetail.getFeatures().add(featureMapper.toDto(productAttribute.getAttribute().getFeature()));
                        checkFeature += 1;
                    }

                    if (level == productAttributes.size()) {
                        productTypeDTO.getTypes().add(
                                ProductTypeDTO.builder()
                                        .name(productAttribute.getAttribute().getValue())
                                        .price(productType.getPrice())
                                        .quantity(productType.getQuantity())
                                        .build()
                        );
                    }

                    if (level == 1) {
                        productTypeDTO.setName(productAttribute.getAttribute().getValue());
                        continue;
                    }
                }
            }

            productDetail.setQuantity(quantity);
            productDetail.getProductTypes().add(productTypeDTO);
        }

        return productDetail;
    }

    @Override
    public void delete(String id) throws I18nException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_PRODUCT_NOT_FOUND)
                            .build();
                });
        product.setStatus(false);
        productRepository.save(product);
    }
}
