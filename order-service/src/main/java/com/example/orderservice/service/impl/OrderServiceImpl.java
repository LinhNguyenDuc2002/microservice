package com.example.orderservice.service.impl;

import com.example.orderservice.constant.I18nMessage;
import com.example.orderservice.constant.OrderDetailStatus;
import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.constant.ProductStatus;
import com.example.orderservice.dto.Error;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.PageDto;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.dto.request.ReceiverRequest;
import com.example.orderservice.entity.Address;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderDetail;
import com.example.orderservice.entity.Receiver;
import com.example.orderservice.exception.InvalidationException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.exception.UnauthorizedException;
import com.example.orderservice.i18n.I18nService;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.payload.productservice.request.WareHouseCheckingReq;
import com.example.orderservice.payload.productservice.response.WareHouseCheckingResponse;
import com.example.orderservice.repository.OrderDetailRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.ReceiverRepository;
import com.example.orderservice.repository.predicate.OrderPredicate;
import com.example.orderservice.security.SecurityUtil;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.ProductService;
import com.example.orderservice.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private I18nService i18nService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReceiverRepository receiverRepository;

    private Lock lock = new ReentrantLock();

    @Override
    public OrderDto create(OrderRequest orderRequest) throws Exception {
        Optional<String> accountId = SecurityUtil.getLoggedInUserId();
        if (!accountId.isPresent()) {
            throw new UnauthorizedException(I18nMessage.ERROR_UNAUTHORIZED);
        }

        List<OrderDetail> orderDetails = orderDetailRepository.findAllById(orderRequest.getDetails());
        if (orderDetails.isEmpty()) {
            throw new NotFoundException(I18nMessage.ERROR_ORDER_DETAIL_NOT_FOUND);
        }

        List<WareHouseCheckingReq> wareHouseCheckingReqs = new ArrayList<>();
        orderDetails.stream().forEach(detail -> {
            wareHouseCheckingReqs.add(
                    WareHouseCheckingReq.builder()
                            .productDetailId(detail.getProductDetailId())
                            .quantity(detail.getQuantity())
                            .build()
            );
        });

        WareHouseCheckingResponse response = productService.checkWarehouse(wareHouseCheckingReqs);
        List<WareHouseCheckingResponse.ProductDetailItem> productDetailItems = response.getProductDetailItems();
        if(response.getStatus().equals(ProductStatus.PRODUCT_NOT_FOUND)) {
            //
        }
        else if (response.getStatus().equals(ProductStatus.PRODUCT_NOT_FOUND)) {
            //
        }

        Receiver receiver = receiverRepository.findById(orderRequest.getReceiverId())
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_RECEIVER_NOT_FOUND);
                });
        Order order = Order.builder()
                .code(generateCode())
                .status(OrderStatus.PROCESSING)
                .receiver(receiver)
                .build();

        orderDetails.stream()
                .forEach(detail -> {
                    detail.setStatus(OrderDetailStatus.PURCHASED);
                    detail.setOrder(order);
                });
        order.setOrderDetails(orderDetails);
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto update(String orderId, String receiverId) throws NotFoundException, InvalidationException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_ORDER_NOT_FOUND);
                });

        if (!OrderStatus.PROCESSING.equals(order.getStatus())) {
            throw new InvalidationException(I18nMessage.ERROR_ORDER_PROCESSED);
        }

        Receiver receiver = receiverRepository.findById(receiverId)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_RECEIVER_NOT_FOUND);
                });

        order.setReceiver(receiver);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public PageDto<OrderDto> getAll(Integer page, Integer size, Date start, Date end, String status, List<String> sortColumns) throws Exception {
        Pageable pageable = (sortColumns == null) ? PageUtil.getPage(page, size) : PageUtil.getPage(page, size, sortColumns.toArray(new String[0]));
        OrderPredicate orderPredicate = new OrderPredicate()
                .from(start)
                .to(end)
                .withStatus(status);
        Page<Order> bills = orderRepository.findAll(orderPredicate.getCriteria(), pageable);

        return PageDto.<OrderDto>builder()
                .index(page)
                .totalPage(bills.getTotalPages())
                .elements(orderMapper.toDtoList(bills.getContent()))
                .build();
    }

    @Override
    public OrderDto get(String id) throws NotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_ORDER_NOT_FOUND);
                });
        return orderMapper.toDto(order);
    }

    @Override
    public PageDto<OrderDto> getByCustomerId(Integer page, Integer size, String status, String id, List<String> sortColumns) throws NotFoundException {
        //check account exist

        Pageable pageable = (sortColumns == null) ? PageUtil.getPage(page, size) : PageUtil.getPage(page, size, sortColumns.toArray(new String[0]));
        OrderPredicate orderPredicate = new OrderPredicate()
                .withCustomerId(id)
                .withStatus(status);
        Page<Order> bills = orderRepository.findAll(orderPredicate.getCriteria(), pageable);

        return PageDto.<OrderDto>builder()
                .index(page)
                .totalPage(bills.getTotalPages())
                .elements(orderMapper.toDtoList(bills.getContent()))
                .build();
    }

    @Override
    public OrderDto changeStatus(String id, String status) throws NotFoundException, InvalidationException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_ORDER_NOT_FOUND);
                });

        if (order.getStatus().equals(OrderStatus.PAID) ||
                order.getStatus().equals(OrderStatus.APPROVED) ||
                !EnumSet.allOf(OrderStatus.class).contains(OrderStatus.valueOf(status))) {
            log.error("{} status is invalid", status);
            throw new InvalidationException(I18nMessage.ERROR_STATUS_INVALID);
        }

        order.setStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);

        /*if (order.getStatus().equals(BillStatus.APPROVED)) {
            Customer customer = new ArrayList<OrderDetail>(order.getOrderDetails()).get(0).getCustomer();
            Map<String, String> emailArgs = new HashMap<>();
            emailArgs.put(EmailConstant.ARG_LOGO_URI, "");
            emailArgs.put(EmailConstant.ARG_BILL_ID, order.getCode());
            emailArgs.put(EmailConstant.ARG_RECEIVER_NAME, customer.getFullname());
            emailArgs.put(EmailConstant.ARG_RECEIVER_PHONE, order.getPhone());
            emailArgs.put(EmailConstant.ARG_DELIVERY_ADDRESS, formatAddress(order.getAddress()));
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
        }*/

        return orderMapper.toDto(order);
    }

    @Override
    public void delete(String id) throws NotFoundException, InvalidationException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_ORDER_NOT_FOUND);
                });

        if (order.getStatus().equals(OrderStatus.APPROVED) ||
                order.getStatus().equals(OrderStatus.PAID)) {
            throw new InvalidationException(I18nMessage.ERROR_ORDER_CAN_NOT_DELETE);
        }
        orderRepository.deleteById(id);
    }

    private String generateCode() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
        return "D" + sdf.format(now);
    }
}
