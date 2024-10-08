package com.example.productservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.productservice.cache.ProductCacheManager;
import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.dto.ExistingProductCheckDTO;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.entity.Category;
import com.example.productservice.entity.Image;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.ProductType;
import com.example.productservice.entity.Shop;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.mapper.ProductTypeMapper;
import com.example.productservice.payload.ProductRequest;
import com.example.productservice.payload.ProductTypeRequest;
import com.example.productservice.payload.UpdateDetailPriceReq;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.repository.ImageRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.ProductTypeRepository;
import com.example.productservice.repository.ShopRepository;
import com.example.productservice.repository.predicate.ProductPredicate;
import com.example.productservice.repository.predicate.ProductTypePredicate;
import com.example.productservice.repository.predicate.ShopPredicate;
import com.example.productservice.security.SecurityUtils;
import com.example.productservice.service.ProductService;
import com.example.productservice.util.DateUtil;
import com.example.productservice.util.PageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductCacheManager productCacheManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaOrderService orderService;

    @Override
    public ProductDTO add(String productRequest, List<MultipartFile> files) throws InvalidException, NotFoundException {
        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        ShopPredicate shopPredicate = new ShopPredicate().withAccountId(userId.get());
        Shop shop = shopRepository.findOne(shopPredicate.getCriteria())
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_SHOP_NOT_FOUND);
                });

        try {
            ProductRequest newProduct = objectMapper.readValue(productRequest, ProductRequest.class);
            if (newProduct == null ||
                    !StringUtils.hasText(newProduct.getName()) ||
                    (newProduct.getPrice() == null && (newProduct.getProductTypes() == null || newProduct.getProductTypes().isEmpty())) ||
                    (newProduct.getQuantity() == null && (newProduct.getProductTypes() == null || newProduct.getProductTypes().isEmpty()))) {
                throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
            }

            Category category = categoryRepository.findById(newProduct.getCategory())
                    .orElseThrow(() -> {
                        return new NotFoundException(ExceptionMessage.ERROR_CATEGORY_NOT_FOUND);
                    });

            Product product = Product.builder()
                    .name(newProduct.getName())
                    .note(newProduct.getNote())
                    .category(category)
                    .shop(shop)
                    .sold(0L)
                    .build();
            Long quantity = newProduct.getQuantity();
            Double price = newProduct.getPrice();
            if (newProduct.getProductTypes() != null && !newProduct.getProductTypes().isEmpty()) {
                List<ProductType> productTypes = new ArrayList<>();
                quantity = 0L;

                for (ProductTypeRequest productTypeRequest : newProduct.getProductTypes()) {
                    ProductType productType = ProductType.builder()
                            .name(productTypeRequest.getName())
                            .description(productTypeRequest.getDescription())
                            .quantity(productTypeRequest.getQuantity())
                            .price(productTypeRequest.getPrice())
                            .product(product)
                            .build();
                    quantity += productType.getQuantity();
                    price = Math.min(price, productType.getPrice());
                    productTypes.add(productType);
                }

                productRepository.save(product);
                productTypeRepository.saveAll(productTypes);
            }
            product.setPrice(price);
            product.setQuantity(quantity);
            productRepository.save(product);

            if (files != null && !files.isEmpty()) {
                List<Image> images = uploadFile(files);
                images.stream().forEach(image -> image.setProduct(product));
                imageRepository.saveAll(images);
            }

            log.info("Added a product");
            return productMapper.toDto(product);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductDTO update(String id, String productRequest, List<MultipartFile> files) throws InvalidException, NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
                });

        try {
            ProductRequest request = objectMapper.readValue(productRequest, ProductRequest.class);
            if (request == null ||
                    !StringUtils.hasText(request.getName()) ||
                    request.getQuantity() == null ||
                    request.getPrice() == null) {
                throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
            }

            Category category = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> {
                        return new NotFoundException(ExceptionMessage.ERROR_CATEGORY_NOT_FOUND);
                    });

            Long quantity = request.getQuantity();
            if (quantity != null && !product.getProductTypes().isEmpty()) {
                Long total = product.getProductTypes().stream().mapToLong(ProductType::getQuantity).sum();
                if (total != quantity) {
                    throw new InvalidException("");
                }
            }

            product.setQuantity(quantity);
            product.setName(request.getName());
            product.setNote(request.getNote());
            product.setCategory(category);
            if (product.getPrice() != request.getPrice() && (product.getProductTypes() == null || product.getProductTypes().isEmpty())) {
                product.setPrice(request.getPrice());
                orderService.updateUnitPrice(
                        UpdateDetailPriceReq.builder()
                                .productId(product.getId())
                                .price(product.getPrice())
                                .build());
            }

            if (files != null && !files.isEmpty()) {
                List<Image> images = product.getImages().stream().toList();
                images.stream().forEach(image -> {
                    try {
                        destroyFile(image.getPublicId(), ObjectUtils.emptyMap());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                imageRepository.deleteAllInBatch(images);

                List<Image> imageFiles = uploadFile(files);
                imageFiles.stream().forEach(image -> image.setProduct(product));
                product.setImages(imageFiles);
            }

            productRepository.save(product);
            return productMapper.toDto(product);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageDTO<ProductDTO> getAll(Integer page, Integer size, String shop, String search, String category, List<String> sortColumns) throws NotFoundException, JsonProcessingException {
        PageDTO<ProductDTO> productCache = productCacheManager.getAllProducts(page, size, shop, category);
        if(productCache != null) return productCache;

        Pageable pageable = (sortColumns == null) ? PageUtil.getPage(page, size) : PageUtil.getPage(page, size, sortColumns.toArray(new String[0]));

        ProductPredicate productPredicate = new ProductPredicate()
                .withShopId(shop)
                .withCategoryId(category)
                .search(search);
        Page<Product> products = productRepository.findAll(productPredicate.getCriteria(), pageable);

        PageDTO pageDTO = PageDTO.<ProductDTO>builder()
                .index(products.getNumber())
                .totalPage(products.getTotalPages())
                .elements(productMapper.toDtoList(products.getContent()))
                .build();
        productCacheManager.saveAllProducts(pageDTO, page, size, shop, category);
        return pageDTO;
    }

    @Override
    public ProductDTO get(String id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
                });

        return productMapper.toDto(product);
    }

    @Override
    public ExistingProductCheckDTO checkProductExist(String id, String productTypeId, Integer quantity) throws NotFoundException, InvalidException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
                });

        Long productQuantity = product.getQuantity();
        Double productPrice = product.getPrice();
        if (StringUtils.hasText(productTypeId)) {
            ProductTypePredicate productTypePredicate = new ProductTypePredicate()
                    .withId(productTypeId)
                    .withProductId(id);
            ProductType productType = productTypeRepository.findOne(productTypePredicate.getCriteria())
                    .orElseThrow(() -> {
                        return new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
                    });

            productQuantity = productType.getQuantity();
            productPrice = productType.getPrice();
        }

        if (quantity > productQuantity) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_SOLD_OUT);
        }

        return ExistingProductCheckDTO.builder()
                .exist(true)
                .price(productPrice)
                .build();
    }

    @Override
    public void delete(String id) throws NotFoundException {
        Optional<Product> product = productRepository.findById(id);

        if (!product.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
        }

        productRepository.deleteById(id);
    }

    public List<Image> uploadFile(List<MultipartFile> files) throws IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            Map data = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            images.add(Image.builder()
                    .format(data.get("format").toString())
                    .resourceType(data.get("resource_type").toString())
                    .secureUrl(data.get("secure_url").toString())
                    .createdAt(DateUtil.convertStringToDate(data.get("created_at").toString()))
                    .url(data.get("url").toString())
                    .publicId(data.get("public_id").toString())
                    .build());
        }
        return images;
    }

    private void destroyFile(String publicId, Map map) throws IOException {
        cloudinary.uploader().destroy(publicId, map);
    }

}
