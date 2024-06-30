package com.example.productservice.util;

import com.example.productservice.payload.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

public final class ResponseUtil<T> {
    public static <T> ResponseEntity<CommonResponse<T>> wrapResponse(T data, String message) {
        CommonResponse<T> response = new CommonResponse<>();

        if(data != null) {
            response.setData(data);
        }

        if(StringUtils.hasText(message)) {
            response.setMessage(message);
        }

        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<CommonResponse<T>> wrapResponse(T data) {
        CommonResponse<T> response = new CommonResponse<>();

        if(data != null) {
            response.setData(data);
        }

        return ResponseEntity.ok(response);
    }
}
