package com.example.apigateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {
    @GetMapping("/userFallBack")
    public String userServiceFallBackMethod() {
        return "User service is taking a longer than expected. Please try again later";
    }
}
