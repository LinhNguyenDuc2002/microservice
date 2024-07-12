package com.example.orderservice.service.impl;

import com.example.orderservice.constant.BillStatus;
import com.example.orderservice.constant.ExceptionMessage;
import com.example.orderservice.dto.BillDTO;
import com.example.orderservice.entity.Address;
import com.example.orderservice.entity.Bill;
import com.example.orderservice.entity.Customer;
import com.example.orderservice.entity.Detail;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.mapper.BillMapper;
import com.example.orderservice.payload.BillRequest;
import com.example.orderservice.payload.UpdateBillRequest;
import com.example.orderservice.payload.productservice.response.ProductCheckingResponse;
import com.example.orderservice.payload.productservice.response.WarehouseCheckingResponse;
import com.example.orderservice.payload.response.PageResponse;
import com.example.orderservice.repository.AddressRepository;
import com.example.orderservice.repository.BillRepository;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.repository.DetailRepository;
import com.example.orderservice.repository.predicate.BillPredicate;
import com.example.orderservice.service.BillService;
import com.example.orderservice.service.ProductService;
import com.example.orderservice.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private ProductService productService;

    private Lock lock = new ReentrantLock();

    @Override
    public BillDTO create(BillRequest billRequest) throws Exception {
        lock.lock();
        try {
            List<Detail> details = detailRepository.findAllById(billRequest.getDetails());

            if (details.isEmpty()) {
                throw new NotFoundException(ExceptionMessage.ERROR_DETAIL_NOT_FOUND);
            }

            Map<String, Integer> productList = new HashMap<>();
            details.stream().forEach(detail -> {
                productList.put(detail.getProduct(), detail.getQuantity());
            });

            if(!productService.checkWarehouse(productList)) {
                throw new InvalidException(ExceptionMessage.ERROR_DETAIL_INVALID_INPUT);
            }

            Bill bill = Bill.builder()
                    .phone(billRequest.getPhone())
                    .status(BillStatus.PROCESSING)
                    .address(Address.builder()
                            .country(billRequest.getAddress().getCountry())
                            .city(billRequest.getAddress().getCity())
                            .district(billRequest.getAddress().getDistrict())
                            .ward(billRequest.getAddress().getWard())
                            .detail(billRequest.getAddress().getDetail())
                            .build())
                    .build();
            billRepository.save(bill);

            for (Detail detail : details) {
                detail.setBill(bill);
                detail.setStatus(true);
            }
            detailRepository.saveAll(details);
            return billMapper.toDto(bill);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public BillDTO update(String id, UpdateBillRequest updateBillRequest) throws InvalidException, NotFoundException {
        Optional<Bill> check = billRepository.findById(id);

        if(!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        Bill bill = check.get();
        if(!BillStatus.PROCESSING.equals(bill.getStatus())) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        if(updateBillRequest.getAddress() == null || updateBillRequest.getPhone() == null) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        addressRepository.delete(bill.getAddress());
        bill.setPhone(updateBillRequest.getPhone());
        bill.setAddress(Address.builder()
                        .country(updateBillRequest.getAddress().getCountry())
                        .city(updateBillRequest.getAddress().getCity())
                        .district(updateBillRequest.getAddress().getDistrict())
                        .ward(updateBillRequest.getAddress().getWard())
                        .detail(updateBillRequest.getAddress().getDetail())
                        .build());

        billRepository.save(bill);
        return billMapper.toDto(bill);
    }

    @Override
    public PageResponse<BillDTO> getAll(Integer page, Integer size, Date start, Date end) {
        Pageable pageable = PageUtil.getPage(page, size);

        BillPredicate billPredicate = new BillPredicate().from(start).to(end);
        Page<Bill> bills = billRepository.findAll(billPredicate.getCriteria(), pageable);
        return PageResponse.<BillDTO>builder()
                .index(page)
                .totalPage(bills.getTotalPages())
                .elements(billMapper.toDtoList(bills.getContent()))
                .build();
    }

    @Override
    public BillDTO get(String id) throws NotFoundException {
        Optional<Bill> bill = billRepository.findById(id);

        if(!bill.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        return billMapper.toDto(bill.get());
    }

    @Override
    public PageResponse<BillDTO> getByCustomerId(Integer page, Integer size, String id) throws NotFoundException {
        Optional<Customer> check = customerRepository.findById(id);

        if(!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        Pageable pageable = PageUtil.getPage(page, size);
        BillPredicate billPredicate = new BillPredicate().customer(id);
        Page<Bill> bills = billRepository.findAll(billPredicate.getCriteria(), pageable);

        return PageResponse.<BillDTO>builder()
                .index(page)
                .totalPage(bills.getTotalPages())
                .elements(billMapper.toDtoList(bills.getContent()))
                .build();
    }

    @Override
    public BillDTO changeStatus(String id, String status) throws NotFoundException, InvalidException {
        Optional<Bill> check = billRepository.findById(id);

        if(!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        if(!EnumSet.allOf(BillStatus.class).contains(BillStatus.valueOf(status))) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        Bill bill = check.get();
        bill.setStatus(BillStatus.valueOf(status));
        billRepository.save(bill);
        return billMapper.toDto(bill);
    }

    @Override
    public void delete(String id) throws NotFoundException {
        Optional<Bill> bill = billRepository.findById(id);

        if(!bill.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        billRepository.deleteById(id);
    }
}
