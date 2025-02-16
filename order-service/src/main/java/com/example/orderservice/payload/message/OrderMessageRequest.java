package com.example.orderservice.payload.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMessageRequest {
    private String status;

    private String id;

    private String message;
}
