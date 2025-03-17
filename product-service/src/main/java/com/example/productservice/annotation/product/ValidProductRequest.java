package com.example.productservice.annotation.product;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ProductRequestValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductRequest {
    String message() default "{error.invalid.product.request}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}