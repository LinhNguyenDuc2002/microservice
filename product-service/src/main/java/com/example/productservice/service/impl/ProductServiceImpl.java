package com.example.productservice.service.impl;

import com.example.productservice.cache.ProductCacheManager;
import com.example.productservice.constant.I18nMessage;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.request.BasicProductRequest;
import com.example.productservice.dto.request.ProductDetailRequest;
import com.example.productservice.dto.request.ProductRequest;
import com.example.productservice.entity.Category;
import com.example.productservice.entity.Image;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.ProductDetail;
import com.example.productservice.exception.InvalidationException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.ProductDetailMapper;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.repository.ImageRepository;
import com.example.productservice.repository.ProductDetailRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.ShopRepository;
import com.example.productservice.repository.predicate.ProductPredicate;
import com.example.productservice.security.SecurityUtils;
import com.example.productservice.service.CloudinaryService;
import com.example.productservice.service.ProductService;
import com.example.productservice.util.PageUtil;
import com.example.productservice.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final String FOLDER = "product-service";
    private final Integer MAX_IMAGE_NUMBER = 4;

    @Autowired
    private ProductDetailRepository productDetailRepository;

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
    public ProductDTO add(ProductRequest productRequest) throws InvalidationException, NotFoundException, IOException {
        Optional<String> userId = SecurityUtils.getLoggedInUserId();

        boolean check = productRepository.existsByName(productRequest.getName());
        if (check) {
            throw new InvalidationException(I18nMessage.ERROR_PRODUCT_NAME_EXISTED);
        }

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_CATEGORY_NOT_FOUND);
                });

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .category(category)
                .status(true)
                .build();

        Map<String, MultipartFile> images = new HashMap<>();
        long totalSize = 0;
        for (MultipartFile file : productRequest.getImages()) {
            totalSize += file.getSize();
            images.put(UUID.randomUUID().toString(), file);
        }
        double totalSizeInMB = totalSize / (1024 * 1024);
        if (productRequest.getImages().size() > MAX_IMAGE_NUMBER || totalSizeInMB > 4) {
            throw new ValidateException(I18nMessage.ERROR_PRODUCT_DETAIL_NAME_DUPLICATED);
        }
        product.setImageIds(StringUtil.joinDelimiter(images.keySet().stream().toList()));

        List<ProductDetail> productDetails = new ArrayList<>();
        for (ProductDetailRequest productDetailRequest : productRequest.getProductDetails()) {
            String imageId = UUID.randomUUID().toString();
            images.put(imageId, productDetailRequest.getImage());
            ProductDetail productDetail = ProductDetail.builder()
                    .name(productDetailRequest.getName())
                    .description(productDetailRequest.getDescription())
                    .quantity(productDetailRequest.getQuantity())
                    .price(productDetailRequest.getPrice())
                    .sold(0L)
                    .status(true)
                    .product(product)
                    .imageId(imageId)
                    .build();
            productDetails.add(productDetail);
        }
        product.setProductDetails(productDetails);
        cloudinaryService.upload(images);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    @Override
    public ProductDTO update(String id, BasicProductRequest productRequest) throws InvalidationException, NotFoundException, IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_PRODUCT_NOT_FOUND);
                });
        if (!productRequest.getName().equals(product.getName())) {
            boolean check = productRepository.existsByName(productRequest.getName());
            if (check) {
                throw new InvalidationException(I18nMessage.ERROR_PRODUCT_NAME_EXISTED);
            }
        }

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_CATEGORY_NOT_FOUND);
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
    public PageDTO<ProductDTO> getAll(Integer page, Integer size, String search, String category, List<String> sortColumns) throws NotFoundException, JsonProcessingException {
//        PageDTO<ProductDTO> productCache = productCacheManager.getAllProducts(page, size, category);
//        if (productCache != null) return productCache;

        Pageable pageable = (sortColumns == null) ? PageUtil.getPage(page, size) : PageUtil.getPage(page, size, sortColumns.toArray(new String[0]));

        ProductPredicate productPredicate = new ProductPredicate()
                .withCategoryId(category)
                .search(search);
        Page<Product> products = productRepository.findAll(productPredicate.getCriteria(), pageable);

        List<ProductDTO> productDTOS = products.getContent().stream()
                .map(product -> {
                    ProductDTO productDTO = productMapper.toDto(product);

                    List<Image> image = imageRepository.findAllById(StringUtil.splitDelimiter(product.getImageIds()));
                    productDTO.setImageUrls(image.stream().map(Image::getSecureUrl).toList());

                    Long quantity = 0L;
                    Long sold = 0L;
                    Double maxPrice = 0.0;
                    for (ProductDetail productDetail : product.getProductDetails()) {
                        quantity += productDetail.getQuantity();
                        sold += productDetail.getSold();
                        if (productDetail.getPrice() > maxPrice) {
                            maxPrice = productDetail.getPrice();
                        }
                    }

                    Double minPrice = product.getProductDetails().stream()
                            .map(ProductDetail::getPrice)
                            .filter(price -> price != null)
                            .min(Double::compareTo)
                            .orElse(0.0);

                    productDTO.setQuantity(quantity);
                    productDTO.setSold(sold);
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
    public ProductDTO get(String id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_PRODUCT_NOT_FOUND);
                });

        ProductDTO productDTO = productMapper.toDto(product);

        List<Image> image = imageRepository.findAllById(StringUtil.splitDelimiter(product.getImageIds()));
        productDTO.setImageUrls(image.stream().map(Image::getSecureUrl).toList());

        Long quantity = 0L;
        Long sold = 0L;
        Double maxPrice = 0.0;
        for (ProductDetail productDetail : product.getProductDetails()) {
            quantity += productDetail.getQuantity();
            sold += productDetail.getSold();
            if (productDetail.getPrice() > maxPrice) {
                maxPrice = productDetail.getPrice();
            }
        }

        Double minPrice = product.getProductDetails().stream()
                .map(ProductDetail::getPrice)
                .filter(price -> price != null)
                .min(Double::compareTo)
                .orElse(0.0);

        productDTO.setQuantity(quantity);
        productDTO.setSold(sold);
        productDTO.setMaxPrice(maxPrice);
        productDTO.setMinPrice(minPrice);

        return productDTO;
    }

    @Override
    public void delete(String id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_PRODUCT_NOT_FOUND);
                });
        product.setStatus(false);
        productRepository.save(product);
    }
}
