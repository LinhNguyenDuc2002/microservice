package com.example.orderservice.service.impl;

import com.example.orderservice.config.ApplicationConfig;
import com.example.orderservice.constant.BillStatus;
import com.example.orderservice.constant.ExceptionMessage;
import com.example.orderservice.constant.KafkaTopic;
import com.example.orderservice.dto.BillDTO;
import com.example.orderservice.entity.Address;
import com.example.orderservice.entity.Bill;
import com.example.orderservice.entity.Customer;
import com.example.orderservice.entity.Detail;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.mapper.BillMapper;
import com.example.orderservice.message.email.EmailConstant;
import com.example.orderservice.message.email.EmailMessage;
import com.example.orderservice.payload.BillRequest;
import com.example.orderservice.payload.UpdateBillRequest;
import com.example.orderservice.dto.PageDTO;
import com.example.orderservice.payload.productservice.request.WareHouseCheckingReq;
import com.example.orderservice.repository.AddressRepository;
import com.example.orderservice.repository.BillRepository;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.repository.DetailRepository;
import com.example.orderservice.repository.predicate.BillPredicate;
import com.example.orderservice.service.BillService;
import com.example.orderservice.service.OrderMessagingService;
import com.example.orderservice.service.ProductService;
import com.example.orderservice.util.PageUtil;
import com.example.orderservice.util.StringFormatUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
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

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private OrderMessagingService messagingService;

    @Autowired
    private ObjectMapper mapper;

    private Lock lock = new ReentrantLock();

    @Override
    public List<BillDTO> create(BillRequest billRequest) throws Exception {
        List<Detail> details = detailRepository.findAllById(billRequest.getDetails());
        if (details.isEmpty()) {
            throw new NotFoundException(ExceptionMessage.ERROR_DETAIL_NOT_FOUND);
        }

        List<WareHouseCheckingReq> wareHouseCheckingReqs = new ArrayList<>();
        details.stream().forEach(detail -> {
            wareHouseCheckingReqs.add(
                    WareHouseCheckingReq.builder()
                            .productId(detail.getProduct())
                            .productTypeId(StringUtils.hasText(detail.getProductType()) ? detail.getProductType() : null)
                            .quantity(detail.getQuantity())
                            .build()
            );
        });

        Map<String, List<String>> response = productService.checkWarehouse(wareHouseCheckingReqs);

        Address address = Address.builder()
                .country(billRequest.getAddress().getCountry())
                .city(billRequest.getAddress().getCity())
                .district(billRequest.getAddress().getDistrict())
                .ward(billRequest.getAddress().getWard())
                .detail(billRequest.getAddress().getDetail())
                .build();
        addressRepository.save(address);

        List<Bill> bills = new ArrayList<>();
        for(String key : response.keySet()) {
            Bill bill = Bill.builder()
                    .phone(billRequest.getPhone())
                    .status(BillStatus.PROCESSING)
                    .address(address)
                    .shopId(key)
                    .build();
            billRepository.save(bill);
            bills.add(bill);

            details.stream()
                    .filter(detail -> response.get(key).contains(detail.getProduct()))
                    .forEach(detail -> {
                        detail.setBill(bill);
                        detail.setStatus(true);
                    });
        }
        detailRepository.saveAll(details);

        return billMapper.toDtoList(bills);
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
    public PageDTO<BillDTO> getAll(String id, Integer page, Integer size, Date start, Date end, String status, List<String> sortColumns) throws Exception {
        boolean checkShop = productService.checkShopExist(id);
        if(!checkShop) {
            throw new NotFoundException(ExceptionMessage.ERROR_SHOP_NOT_FOUND);
        }

        Pageable pageable = (sortColumns == null) ? PageUtil.getPage(page, size) : PageUtil.getPage(page, size, sortColumns.toArray(new String[0]));
        BillPredicate billPredicate = new BillPredicate()
                .from(start)
                .to(end)
                .withStatus(status)
                .withShopId(id);
        Page<Bill> bills = billRepository.findAll(billPredicate.getCriteria(), pageable);

        return PageDTO.<BillDTO>builder()
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
    public PageDTO<BillDTO> getByCustomerId(Integer page, Integer size, String status, String id, List<String> sortColumns) throws NotFoundException {
        Optional<Customer> check = customerRepository.findById(id);
        if(!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_CUSTOMER_NOT_FOUND);
        }

        Pageable pageable = (sortColumns == null) ? PageUtil.getPage(page, size) : PageUtil.getPage(page, size, sortColumns.toArray(new String[0]));
        BillPredicate billPredicate = new BillPredicate()
                .withCustomerId(id)
                .withStatus(status);
        Page<Bill> bills = billRepository.findAll(billPredicate.getCriteria(), pageable);

        return PageDTO.<BillDTO>builder()
                .index(page)
                .totalPage(bills.getTotalPages())
                .elements(billMapper.toDtoList(bills.getContent()))
                .build();
    }

    @Override
    public BillDTO changeStatus(String id, String status) throws NotFoundException, InvalidException, JsonProcessingException {
        Optional<Bill> check = billRepository.findById(id);
        if(!check.isPresent()) {
            log.error("Bill {} is not found", id);
            throw new NotFoundException(ExceptionMessage.ERROR_BILL_NOT_FOUND);
        }

        Bill bill = check.get();
        if(bill.getStatus().equals(BillStatus.PAID) ||
                bill.getStatus().equals(BillStatus.APPROVED) ||
                !EnumSet.allOf(BillStatus.class).contains(BillStatus.valueOf(status))) {
            log.error("{} status is invalid", status);
            throw new InvalidException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        bill.setStatus(BillStatus.valueOf(status));
        billRepository.save(bill);

        if(bill.getStatus().equals(BillStatus.APPROVED)) {
            Customer customer = new ArrayList<Detail>(bill.getDetails()).get(0).getCustomer();
            Map<String, String> emailArgs = new HashMap<>();
            emailArgs.put(EmailConstant.ARG_LOGO_URI, "");
            emailArgs.put(EmailConstant.ARG_BILL_ID, bill.getCode());
            emailArgs.put(EmailConstant.ARG_RECEIVER_NAME, customer.getFullname());
            emailArgs.put(EmailConstant.ARG_RECEIVER_PHONE, bill.getPhone());
            emailArgs.put(EmailConstant.ARG_DELIVERY_ADDRESS, formatAddress(bill.getAddress()));
            emailArgs.put(EmailConstant.ARG_SUPPORT_EMAIL, applicationConfig.getSenderEmail());

            EmailMessage email = EmailMessage.builder()
                    .template(EmailConstant.TEMPLATE_EMAIL_CONFIRM_ORDER)
                    .receiver(customer.getEmail())
                    .sender(applicationConfig.getSenderEmail())
                    .subject(EmailConstant.ARG_CONFIRM_ORDER_SUBJECT)
                    .args(emailArgs)
                    .locale(LocaleContextHolder.getLocale())
                    .build();
            messagingService.sendMessage(KafkaTopic.SEND_EMAIL, mapper.writeValueAsString(email));
        }

        return billMapper.toDto(bill);
    }

    @Override
    public void delete(String id) throws NotFoundException {
        Optional<Bill> bill = billRepository.findById(id);

        if(!bill.isPresent() ||
                bill.get().getStatus().equals(BillStatus.APPROVED) ||
                bill.get().getStatus().equals(BillStatus.PAID)) {
            throw new NotFoundException(ExceptionMessage.ERROR_PRODUCT_INVALID_INPUT);
        }

        billRepository.deleteById(id);
    }

    private String formatAddress(Address address) {
        String detail = address.getDetail();

        return detail + "\n" +
                address.getWard() + " - " + address.getDistrict() + " - " + address.getCity() + " - " + address.getCountry();
    }
}
