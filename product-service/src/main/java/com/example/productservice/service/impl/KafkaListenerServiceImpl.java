//package com.example.productservice.service.impl;
//
//import com.example.productservice.constant.KafkaTopic;
//import com.example.productservice.constant.MessageStatus;
//import com.example.productservice.entity.ProductType;
//import com.example.productservice.payload.message.OrderMessage;
//import com.example.productservice.payload.message.OrderMessageResponse;
//import com.example.productservice.payload.orderservice.request.ProductCheckingRequest;
//import com.example.productservice.repository.ProductTypeRepository;
//import com.example.productservice.repository.predicate.ProductTypePredicate;
//import com.example.productservice.service.KafkaListenerService;
//import com.example.productservice.service.OrderMessagingService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//public class KafkaListenerServiceImpl implements KafkaListenerService {
//    @Autowired
//    private ObjectMapper mapper;
//
//    @Autowired
//    private OrderMessagingService orderMessagingService;
//
//    @Autowired
//    private ProductTypeRepository productTypeRepository;
//
//    @KafkaListener(topics = KafkaTopic.INVENTORY_DEDUCT)
//    @Override
//    public void updateInventory(String request) throws JsonProcessingException {
//        OrderMessage orderMessage = mapper.readValue(request, OrderMessage.class);
//        Map<String, Integer> requestMap = orderMessage.getProductDetails().stream()
//                .collect(Collectors.toMap(
//                        ProductCheckingRequest::getProductDetailId,
//                        ProductCheckingRequest::getQuantity
//                ));
//
//        ProductTypePredicate productTypePredicate = new ProductTypePredicate()
//                .inIds(requestMap.keySet().stream().toList())
//                .withStatus(true);
//        List<ProductType> productTypes = productTypeRepository.findAll(productTypePredicate.getCriteria());
//
//        boolean check = true;
//        if (productTypes.size() != requestMap.size()) {
//            check = false;
//        } else {
//            for (ProductType productType : productTypes) {
//                String id = productType.getId();
//                Integer quantity = productType.getQuantity();
//                Integer purchased = requestMap.get(id);
//                if (quantity >= purchased) {
//                    productType.setQuantity(quantity - purchased);
//                }
//                else {
//                    check = false;
//                    break;
//                }
//            }
//        }
//
//        OrderMessageResponse response = OrderMessageResponse.builder()
//                .id(orderMessage.getId())
//                .build();
//        if (check) {
//            productTypeRepository.saveAll(productTypes);
//            response.setStatus(MessageStatus.INVENTORY_DEDUCT_SUCCESS.name());
//        } else {
//            response.setStatus(MessageStatus.INVENTORY_DEDUCT_FAIL.name());
//        }
//        orderMessagingService.updateOrderStatus(response);
//    }
//}
