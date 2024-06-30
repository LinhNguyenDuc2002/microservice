package com.example.productservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.entity.Category;
import com.example.productservice.entity.Image;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.Shop;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.payload.ProductRequest;
import com.example.productservice.payload.response.PageResponse;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.repository.ImageRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.ShopRepository;
import com.example.productservice.repository.predicate.ProductPredicate;
import com.example.productservice.service.ProductService;
import com.example.productservice.util.DateUtil;
import com.example.productservice.util.PageUtil;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Override
    public ProductDTO add(String productRequest, List<MultipartFile> files) throws InvalidException, NotFoundException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductRequest newProduct = objectMapper.readValue(productRequest, ProductRequest.class);

            if (newProduct == null ||
                    !StringUtils.hasText(newProduct.getName()) ||
                    newProduct.getQuantity() == null ||
                    newProduct.getPrice() == null) {
                throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
            }

            Category category = categoryRepository.findById(newProduct.getCategory())
                    .orElseThrow(() -> {
                        return new NotFoundException(ExceptionMessage.ERROR_CATEGORY_NOT_FOUND);
                    });

//            Shop shop = shopRepository.
            Product product = Product.builder()
                    .name(newProduct.getName())
                    .price(newProduct.getPrice())
                    .quantity(newProduct.getQuantity())
                    .note(newProduct.getNote())
                    .category(category)
                    .build();

            product.setSold(0L);
            productRepository.save(product);

            List<Image> images = uploadFile(files);
            images.stream().forEach(image -> image.setProduct(product));
            imageRepository.saveAll(images);

            log.info("Added a product");
            return productMapper.toDto(product);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductDTO update(String id, String productRequest, List<MultipartFile> files) throws InvalidException, NotFoundException {
        Optional<Product> check = productRepository.findById(id);

        if(!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
        }

        Product product = check.get();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
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

            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setQuantity(request.getQuantity());
            product.setNote(request.getNote());
            product.setCategory(category);

            if(files != null && !files.isEmpty()) {
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
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<ProductDTO> getAll(Integer page, Integer size, String shop, String search, String category) throws NotFoundException {
        Pageable pageable = PageUtil.getPage(page, size);

        ProductPredicate productPredicate = new ProductPredicate()
                .shop(shop)
                .category(category)
                .search(search);
        Page<Product> products = productRepository.findAll(productPredicate.getCriteria(), pageable);

        PageResponse pageResponse = PageResponse.<ProductDTO>builder()
                .index(page)
                .totalPage(products.getTotalPages())
                .elements(productMapper.toDtoList(products.getContent()))
                .build();

        return pageResponse;
    }

    @Override
    public ProductDTO get(String id) throws NotFoundException {
        Optional<Product> product = productRepository.findById(id);

        if(!product.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_NOT_FOUND);
        }

        return productMapper.toDto(product.get());
    }

    @Override
    public void delete(String id) throws NotFoundException {
        Optional<Product> product = productRepository.findById(id);

        if(!product.isPresent()) {
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
