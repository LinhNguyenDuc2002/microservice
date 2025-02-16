package com.example.productservice.payload.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderMessageResponse {
    private String status;

    private String id;

    private String message;
}
