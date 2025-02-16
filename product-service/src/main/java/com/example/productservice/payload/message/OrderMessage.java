package com.example.productservice.payload.message;

import com.example.productservice.payload.orderservice.request.ProductCheckingRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderMessage {
    private String id;

    private List<ProductCheckingRequest> productDetails;
}
