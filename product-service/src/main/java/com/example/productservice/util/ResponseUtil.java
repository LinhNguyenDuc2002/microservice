package com.example.productservice.util;

import com.example.productservice.constant.Status;
import com.example.productservice.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseUtil<T> {
    public static <T> ResponseEntity<Response<T>> wrapResponse(T data, String message) {
        Response<T> response = Response.<T>builder()
                .status(Status.SUCCESS.getMessage())
                .code(HttpStatus.OK.value())
                .message(message)
                .build();
        if (data != null) {
            response.setData(data);
        }

        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<Response<T>> wrapResponse(String message) {
        Response<T> response = Response.<T>builder()
                .status(Status.SUCCESS.getMessage())
                .code(HttpStatus.OK.value())
                .message(message)
                .build();
        return ResponseEntity.ok(response);
    }
}
