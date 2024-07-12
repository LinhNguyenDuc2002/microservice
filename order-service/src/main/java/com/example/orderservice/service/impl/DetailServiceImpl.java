package com.example.orderservice.service.impl;

import com.example.orderservice.constant.BillStatus;
import com.example.orderservice.constant.ExceptionMessage;
import com.example.orderservice.dto.CheckingDetailDTO;
import com.example.orderservice.dto.DetailDTO;
import com.example.orderservice.entity.Customer;
import com.example.orderservice.entity.Detail;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.mapper.DetailMapper;
import com.example.orderservice.payload.productservice.response.ProductCheckingResponse;
import com.example.orderservice.payload.response.PageResponse;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.repository.DetailRepository;
import com.example.orderservice.repository.predicate.DetailPredicate;
import com.example.orderservice.service.DetailService;
import com.example.orderservice.service.ProductService;
import com.example.orderservice.util.PageUtil;
import com.example.orderservice.webclient.WebClientProcessor;
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
    private CustomerRepository customerRepository;

    @Autowired
    private DetailMapper detailMapper;

    @Autowired
    private ProductService productService;

    @Override
    public DetailDTO create(String productId, String customerId, Integer quantity) throws Exception {
        Optional<Customer> checkCustomer = customerRepository.findById(customerId);

        if (!checkCustomer.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        if (quantity <= 0) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        ProductCheckingResponse productCheckingResponse = productService.checkProductExist(productId, quantity);

        if(!productCheckingResponse.isExist()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        Detail detail = Detail.builder()
                .customer(checkCustomer.get())
                .product(productId)
                .quantity(quantity)
                .unitPrice(productCheckingResponse.getPrice())
                .status(false)
                .build();
        detailRepository.save(detail);

        return detailMapper.toDto(detail);
    }

    @Override
    public DetailDTO update(String id, Integer quantity) throws Exception {
        Optional<Detail> check = detailRepository.findById(id);

        if(!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_DETAIL_NOT_FOUND);
        }

        if(quantity <= 0) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        Detail detail = check.get();
        ProductCheckingResponse productCheckingResponse = productService.checkProductExist(detail.getProduct(), quantity);
        if(!productCheckingResponse.isExist()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
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

    @Override
    public CheckingDetailDTO checkDetailExist(String id) throws NotFoundException, InvalidException {
        Detail detail = detailRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_DETAIL_NOT_FOUND);
                });

        if(!detail.isStatus() || !detail.getBill().getStatus().equals(BillStatus.PAID)) {
            throw new InvalidException("");
        }

        return new CheckingDetailDTO(detail.getCustomer().getAccountId(), detail.getProduct());
    }
}
