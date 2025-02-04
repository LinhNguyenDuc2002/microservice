package com.example.orderservice.service.impl;

import com.example.orderservice.constant.I18nMessage;
import com.example.orderservice.constant.OrderDetailStatus;
import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.constant.ProductStatus;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.PageDto;
import com.example.orderservice.dto.request.OrderProductRequest;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.entity.OrderDetail;
import com.example.orderservice.entity.PurchaseOrder;
import com.example.orderservice.entity.Receiver;
import com.example.orderservice.exception.InvalidationException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.exception.UnauthorizedException;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.payload.productservice.request.ProductCheckingReq;
import com.example.orderservice.payload.productservice.response.ProductCheckingResponse;
import com.example.orderservice.payload.productservice.response.ProductListCheckingResponse;
import com.example.orderservice.repository.OrderDetailRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.ReceiverRepository;
import com.example.orderservice.repository.predicate.OrderPredicate;
import com.example.orderservice.security.SecurityUtil;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.ProductService;
import com.example.servicefoundation.i18n.I18nService;
import com.example.servicefoundation.util.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

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

        List<ProductCheckingReq> productCheckingReqs = new ArrayList<>();
        orderDetails.stream().forEach(detail -> {
            productCheckingReqs.add(
                    ProductCheckingReq.builder()
                            .productDetailId(detail.getProductDetailId())
                            .quantity(detail.getQuantity())
                            .build()
            );
        });

        ProductListCheckingResponse response = productService.checkWarehouse(productCheckingReqs);
        List<ProductListCheckingResponse.ProductDetailItem> productDetailItems = response.getProductDetailItems();
        if (response.getStatus().equals(ProductStatus.PRODUCT_NOT_FOUND)) {
            //
        } else if (response.getStatus().equals(ProductStatus.PRODUCT_NOT_FOUND)) {
            //
        }

        Receiver receiver = receiverRepository.findById(orderRequest.getReceiverId())
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_RECEIVER_NOT_FOUND);
                });
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .code(generateCode())
                .status(OrderStatus.PROCESSING)
                .receiver(receiver)
                .build();

        orderDetails.stream()
                .forEach(detail -> {
                    detail.setStatus(OrderDetailStatus.PURCHASED);
                    detail.setPurchaseOrder(purchaseOrder);
                });
        purchaseOrder.setOrderDetails(orderDetails);
        orderRepository.save(purchaseOrder);

        // send mail to confirm order created

        return orderMapper.toDto(purchaseOrder);
    }

    @Override
    public OrderDto create(OrderProductRequest orderProductRequest) throws Exception {
        Optional<String> accountId = SecurityUtil.getLoggedInUserId();
        if (!accountId.isPresent()) {
            throw new UnauthorizedException(I18nMessage.ERROR_UNAUTHORIZED);
        }

        ProductCheckingReq productCheckingReq = ProductCheckingReq.builder()
                .productDetailId(orderProductRequest.getProductDetailId())
                .quantity(orderProductRequest.getQuantity())
                .build();
        ProductCheckingResponse response = productService.checkProduct(productCheckingReq);

        if (!response.isExist()) {
            throw new NotFoundException(response.getInfo());
        }

        Receiver receiver = receiverRepository.findById(orderProductRequest.getReceiverId())
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_RECEIVER_NOT_FOUND);
                });

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(orderProductRequest.getQuantity())
                .unitPrice(response.getPrice())
                .productDetailId(orderProductRequest.getProductDetailId())
                .status(OrderDetailStatus.PURCHASED)
                .commentStatus(false)
                .customerId(accountId.get())
                .build();
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .code(generateCode())
                .status(OrderStatus.PROCESSING)
                .receiver(receiver)
                .orderDetails(List.of(orderDetail))
                .build();
        orderDetail.setPurchaseOrder(purchaseOrder);
        orderRepository.save(purchaseOrder);

        // send mail to confirm order created

        return orderMapper.toDto(purchaseOrder);
    }

    @Override
    public OrderDto update(String orderId, String receiverId) throws NotFoundException, InvalidationException {
        PurchaseOrder purchaseOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_ORDER_NOT_FOUND);
                });

        if (!OrderStatus.PROCESSING.equals(purchaseOrder.getStatus())) {
            throw new InvalidationException(I18nMessage.ERROR_ORDER_PROCESSED);
        }

        Receiver receiver = receiverRepository.findById(receiverId)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_RECEIVER_NOT_FOUND);
                });

        purchaseOrder.setReceiver(receiver);
        orderRepository.save(purchaseOrder);
        return orderMapper.toDto(purchaseOrder);
    }

    @Override
    public PageDto<OrderDto> getAll(Integer page, Integer size, Date start, Date end, String status, List<String> sortColumns) throws Exception {
        Pageable pageable = (sortColumns == null) ? PaginationUtil.getPage(page, size) : PaginationUtil.getPage(page, size, sortColumns.toArray(new String[0]));
        OrderPredicate orderPredicate = new OrderPredicate()
                .from(start)
                .to(end)
                .withStatus(status);
        Page<PurchaseOrder> bills = orderRepository.findAll(orderPredicate.getCriteria(), pageable);

        return PageDto.<OrderDto>builder()
                .index(page)
                .totalPage(bills.getTotalPages())
                .elements(orderMapper.toDtoList(bills.getContent()))
                .build();
    }

    @Override
    public OrderDto get(String id) throws NotFoundException {
        PurchaseOrder purchaseOrder = orderRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_ORDER_NOT_FOUND);
                });
        return orderMapper.toDto(purchaseOrder);
    }

    @Override
    public PageDto<OrderDto> getByCustomerId(Integer page, Integer size, String status, String id, List<String> sortColumns) throws NotFoundException {
        //check account exist

        Pageable pageable = (sortColumns == null) ? PaginationUtil.getPage(page, size) : PaginationUtil.getPage(page, size, sortColumns.toArray(new String[0]));
        OrderPredicate orderPredicate = new OrderPredicate()
                .withCustomerId(id)
                .withStatus(status);
        Page<PurchaseOrder> bills = orderRepository.findAll(orderPredicate.getCriteria(), pageable);

        return PageDto.<OrderDto>builder()
                .index(page)
                .totalPage(bills.getTotalPages())
                .elements(orderMapper.toDtoList(bills.getContent()))
                .build();
    }

    @Override
    public OrderDto changeStatus(String id, String status) throws NotFoundException, InvalidationException {
        PurchaseOrder purchaseOrder = orderRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_ORDER_NOT_FOUND);
                });

        if (purchaseOrder.getStatus().equals(OrderStatus.PAID) ||
                purchaseOrder.getStatus().equals(OrderStatus.APPROVED) ||
                !EnumSet.allOf(OrderStatus.class).contains(OrderStatus.valueOf(status))) {
            log.error("{} status is invalid", status);
            throw new InvalidationException(I18nMessage.ERROR_STATUS_INVALID);
        }

        purchaseOrder.setStatus(OrderStatus.valueOf(status));
        orderRepository.save(purchaseOrder);

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

        return orderMapper.toDto(purchaseOrder);
    }

    @Override
    public void delete(String id) throws NotFoundException, InvalidationException {
        PurchaseOrder purchaseOrder = orderRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_ORDER_NOT_FOUND);
                });

        if (purchaseOrder.getStatus().equals(OrderStatus.APPROVED) ||
                purchaseOrder.getStatus().equals(OrderStatus.PAID)) {
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
