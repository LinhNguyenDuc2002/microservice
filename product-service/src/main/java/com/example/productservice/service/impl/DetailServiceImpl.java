package com.example.productservice.service.impl;

import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.dto.DetailDTO;
import com.example.productservice.entity.Customer;
import com.example.productservice.entity.Detail;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.DetailMapper;
import com.example.productservice.payload.response.PageResponse;
import com.example.productservice.repository.CustomerRepository;
import com.example.productservice.repository.DetailRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.predicate.DetailPredicate;
import com.example.productservice.service.DetailService;
import com.example.productservice.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DetailServiceImpl implements DetailService {
    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DetailMapper detailMapper;

    @Override
    public DetailDTO create(String productId, String customerId, Integer quantity) throws NotFoundException, InvalidException {
        Optional<Customer> checkCustomer = customerRepository.findById(customerId);

        if (!checkCustomer.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        Optional<Product> checkProduct = productRepository.findById(productId);

        if (!checkProduct.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        if (quantity <= 0) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        Product product = checkProduct.get();
        if(quantity > product.getQuantity()) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        Detail detail = Detail.builder()
                .customer(checkCustomer.get())
                .product(product)
                .quantity(quantity)
                .unitPrice(product.getPrice())
                .status(false)
                .build();
        detailRepository.save(detail);

        return detailMapper.toDto(detail);
    }

    @Override
    public DetailDTO update(String id, Integer quantity) throws NotFoundException, InvalidException {
        Optional<Detail> check = detailRepository.findById(id);

        if(!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        if(quantity <= 0) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        Detail detail = check.get();
        if(quantity > detail.getProduct().getQuantity()) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        detail.setQuantity(quantity);
        detailRepository.save(detail);

        return detailMapper.toDto(detail);
    }

    @Override
    public PageResponse<DetailDTO> getAll(Integer page, Integer size, Boolean status) {
        Pageable pageable = PageUtil.getPage(page, size);

        DetailPredicate detailPredicate = new DetailPredicate().status(status);
        Page<Detail> details = detailRepository.findAll(detailPredicate.getCriteria(), pageable);

        return PageResponse.<DetailDTO>builder()
                .index(page)
                .totalPage(details.getTotalPages())
                .elements(detailMapper.toDtoList(details.getContent()))
                .build();
    }

    @Override
    public DetailDTO get(String id) throws NotFoundException {
        Optional<Detail> check = detailRepository.findById(id);

        if(!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        return detailMapper.toDto(check.get());
    }

    @Override
    public void delete(String id) throws NotFoundException {
        Optional<Detail> check = detailRepository.findById(id);

        if(!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        detailRepository.deleteById(id);
    }
}
