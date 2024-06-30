package com.example.productservice.service.impl;

import com.example.productservice.constant.BillStatus;
import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.dto.BillDTO;
import com.example.productservice.entity.Address;
import com.example.productservice.entity.Bill;
import com.example.productservice.entity.Customer;
import com.example.productservice.entity.Detail;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.BillMapper;
import com.example.productservice.payload.BillRequest;
import com.example.productservice.payload.UpdateBillRequest;
import com.example.productservice.payload.response.PageResponse;
import com.example.productservice.repository.AddressRepository;
import com.example.productservice.repository.BillRepository;
import com.example.productservice.repository.CustomerRepository;
import com.example.productservice.repository.DetailRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.predicate.BillPredicate;
import com.example.productservice.service.BillService;
import com.example.productservice.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

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
    private ProductRepository productRepository;

    @Autowired
    private BillMapper billMapper;

    @Override
    public BillDTO create(BillRequest billRequest) throws InvalidException {
        List<Detail> details = detailRepository.findAllById(billRequest.getDetails());

        if(details.isEmpty()) {
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        for(Detail detail : details) {
            Product product = detail.getProduct();

            if(detail.getQuantity() > product.getQuantity()) {
                throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
            }

            product.setQuantity(product.getQuantity() - detail.getQuantity());
            product.setSold(product.getSold() + detail.getQuantity());
            productRepository.save(product);
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

        for(Detail detail : details) {
            detail.setBill(bill);
            detail.setStatus(true);
        }
        detailRepository.saveAll(details);
        return billMapper.toDto(bill);
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
