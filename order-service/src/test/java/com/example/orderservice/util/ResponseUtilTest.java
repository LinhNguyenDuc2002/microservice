package com.example.orderservice.util;

import com.example.orderservice.payload.response.CommonResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseUtilTest {
    @Test
    void test_wrapResponse() {
        String data = "Test";
        String message = "Test successfully";

        ResponseEntity<CommonResponse<Object>> response = ResponseUtil.wrapResponse(data, message);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(data, response.getBody().getData());
        assertEquals(message, response.getBody().getMessage());
    }

    @Test
    void test_wrapResponse_dataIsNull() {
        String message = "Test successfully";

        ResponseEntity<CommonResponse<Object>> response = ResponseUtil.wrapResponse(null, message);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody().getData());
        assertEquals(message, response.getBody().getMessage());
    }

    @Test
    void test_wrapResponse_noMessage() {
        String data = "Test";

        ResponseEntity<CommonResponse<Object>> response = ResponseUtil.wrapResponse(data);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(data, response.getBody().getData());
    }

    @Test
    void test_wrapResponse_noMessage_dataIsNull() {
        ResponseEntity<CommonResponse<Object>> response = ResponseUtil.wrapResponse(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody().getData());
    }
}
