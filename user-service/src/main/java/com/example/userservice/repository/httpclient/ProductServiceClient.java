package com.example.userservice.repository.httpclient;

import com.example.userservice.payload.CustomerRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", url = "${app.services.product}")
public interface ProductServiceClient {
    @PostMapping(value = "/customer", produces = MediaType.APPLICATION_JSON_VALUE)
    void createCustomer(@RequestBody CustomerRequest customerRequest);
}
