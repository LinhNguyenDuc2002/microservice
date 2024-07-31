package com.example.orderservice.service.impl;

import com.example.orderservice.constant.BillStatus;
import com.example.orderservice.constant.ExceptionMessage;
import com.example.orderservice.dto.CheckingDetailDTO;
import com.example.orderservice.dto.DetailDTO;
import com.example.orderservice.dto.ShopDetailDTO;
import com.example.orderservice.entity.Bill;
import com.example.orderservice.entity.Customer;
import com.example.orderservice.entity.Detail;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.mapper.DetailMapper;
import com.example.orderservice.payload.productservice.response.ProductCheckingResponse;
import com.example.orderservice.dto.PageDTO;
import com.example.orderservice.repository.BillRepository;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.repository.DetailRepository;
import com.example.orderservice.repository.predicate.DetailPredicate;
import com.example.orderservice.service.DetailService;
import com.example.orderservice.service.ProductService;
import com.example.orderservice.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private BillRepository billRepository;

    @Override
    public DetailDTO create(String productId, String customerId, Integer quantity) throws Exception {
        if (quantity <= 0) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        Optional<Customer> checkCustomer = customerRepository.findByAccountId(customerId);
        if (!checkCustomer.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_CUSTOMER_NOT_FOUND);
        }

        ProductCheckingResponse productCheckingResponse = productService.checkProductExist(productId, quantity);
        if (!productCheckingResponse.isExist()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        Detail detail = Detail.builder()
                .product(productId)
                .quantity(quantity)
                .unitPrice(productCheckingResponse.getPrice())
                .customer(checkCustomer.get())
                .status(false)
                .commentStatus(false)
                .build();
        detailRepository.save(detail);

        return detailMapper.toDto(detail);
    }

    @Override
    public DetailDTO update(String id, Integer quantity) throws Exception {
        Detail detail = detailRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_DETAIL_NOT_FOUND);
                });

        if (quantity <= 0) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        ProductCheckingResponse productCheckingResponse = productService.checkProductExist(detail.getProduct(), quantity);
        if (!productCheckingResponse.isExist()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        detail.setQuantity(quantity);
        detailRepository.save(detail);

        return detailMapper.toDto(detail);
    }

    @Override
    public PageDTO<ShopDetailDTO> getAll(Integer page, Integer size, String customerId, Boolean status) throws Exception {
        Pageable pageable = PageUtil.getPage(page, size);

        DetailPredicate detailPredicate = new DetailPredicate()
                .withStatus(status)
                .withCustomerId(customerId);
        Page<Detail> details = detailRepository.findAll(detailPredicate.getCriteria(), pageable);

        Map<String, Integer> productList = new LinkedHashMap<>();
        details.stream().forEach(detail -> {
            productList.put(detail.getProduct(), detail.getQuantity());
        });
        Map<String, List<String>> response = productService.checkWarehouse(productList);

        List<ShopDetailDTO> shopDetailDTOS = new ArrayList<>();
        for (String key : response.keySet()) {
            List<Detail> responseDetail = details.stream()
                    .filter(detail -> response.get(key).contains(detail.getProduct()))
                    .toList();

            shopDetailDTOS.add(
                    ShopDetailDTO.builder()
                            .id(key)
                            .status(status)
                            .details(detailMapper.toDtoList(responseDetail))
                            .build()
            );
        }

        return PageDTO.<ShopDetailDTO>builder()
                .index(page)
                .totalPage(details.getTotalPages())
                .elements(shopDetailDTOS)
                .build();
    }

    @Override
    public DetailDTO get(String id) throws NotFoundException {
        Optional<Detail> check = detailRepository.findById(id);

        if (!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        return detailMapper.toDto(check.get());
    }

    @Override
    public void delete(String id) throws NotFoundException {
        Optional<Detail> check = detailRepository.findById(id);

        if (!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        detailRepository.deleteById(id);
    }

    @Override
    public CheckingDetailDTO checkDetailExist(String id) throws NotFoundException {
        DetailPredicate detailPredicate = new DetailPredicate()
                .withId(id)
                .withCommentStatus(false)
                .withStatus(true);
        Detail detail = detailRepository.findOne(detailPredicate.getCriteria())
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_DETAIL_NOT_FOUND);
                });

        Bill bill = detail.getBill();
        if (!bill.getStatus().equals(BillStatus.PAID)) {
            throw new NotFoundException(ExceptionMessage.ERROR_BILL_STATUS_INVALID);
        }

        return new CheckingDetailDTO(detail.getProduct(), detail.getCustomer().getAccountId());
    }
}
