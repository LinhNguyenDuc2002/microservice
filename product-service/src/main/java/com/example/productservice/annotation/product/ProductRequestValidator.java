package com.example.productservice.annotation.product;

import com.example.productservice.constant.I18nMessage;
import com.example.productservice.dto.request.ProductFormRequest;
import com.example.productservice.dto.request.ProductRequest;
import com.example.productservice.dto.request.ProductTypeRequest;
import com.example.productservice.dto.request.SubTypeRequest;
import com.example.servicefoundation.i18n.I18nService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;

public class ProductRequestValidator implements ConstraintValidator<ValidProductRequest, ProductFormRequest> {
    @Autowired
    private I18nService i18nService;

    @Override
    public boolean isValid(ProductFormRequest request, ConstraintValidatorContext context) {
        boolean valid = true;

        ProductRequest productRequest = request.getProduct();
        if (request.getTypes().isEmpty()) {
            if(productRequest.getPrice() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                i18nService.getMessage("error.not-null", LocaleContextHolder.getLocale())
                        )
                        .addPropertyNode("price")
                        .addConstraintViolation();
                valid = false;
            } else if (productRequest.getPrice() < 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                i18nService.getMessage("error.price.negative", LocaleContextHolder.getLocale())
                        )
                        .addPropertyNode("price")
                        .addConstraintViolation();
                valid = false;
            } else if (productRequest.getPrice() > Integer.MAX_VALUE) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                i18nService.getMessage("error.price.max", LocaleContextHolder.getLocale())
                        )
                        .addPropertyNode("price")
                        .addConstraintViolation();
                valid = false;
            }

            if(productRequest.getQuantity() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                i18nService.getMessage("error.not-null", LocaleContextHolder.getLocale())
                        )
                        .addPropertyNode("quantity")
                        .addConstraintViolation();
                valid = false;
            } else if (productRequest.getQuantity() < 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                i18nService.getMessage("error.quantity.negative", LocaleContextHolder.getLocale())
                        )
                        .addPropertyNode("price")
                        .addConstraintViolation();
                valid = false;
            } else if (productRequest.getQuantity() > Integer.MAX_VALUE) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                i18nService.getMessage("error.quantity.max", LocaleContextHolder.getLocale())
                        )
                        .addPropertyNode("price")
                        .addConstraintViolation();
                valid = false;
            }

            return valid;
        }

//        List<ProductTypeRequest> productTypes = productRequest.getProductTypes();
//        if(!request.getTypes().isEmpty() && productTypes.isEmpty()) {
//            context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate(
//                            i18nService.getMessage("error.not-empty", LocaleContextHolder.getLocale())
//                    )
//                    .addPropertyNode("productTypes")
//                    .addConstraintViolation();
//            valid = false;
//            return valid;
//        }
//
//        if(!productTypes.isEmpty()) {
//            if(productTypes.get(0).getTypes().isEmpty()) {
//                for(ProductTypeRequest productType : productTypes) {
//                    if(productType.getPrice() == null) {
//                        context.disableDefaultConstraintViolation();
//                        context.buildConstraintViolationWithTemplate(
//                                        i18nService.getMessage("error.not-null", LocaleContextHolder.getLocale())
//                                )
//                                .addPropertyNode("price")
//                                .addConstraintViolation();
//                        valid = false;
//                    } else if (productType.getPrice() < 0) {
//                        context.disableDefaultConstraintViolation();
//                        context.buildConstraintViolationWithTemplate(
//                                        i18nService.getMessage("error.price.negative", LocaleContextHolder.getLocale())
//                                )
//                                .addPropertyNode("price")
//                                .addConstraintViolation();
//                        valid = false;
//                    } else if (productType.getPrice() > Integer.MAX_VALUE) {
//                        context.disableDefaultConstraintViolation();
//                        context.buildConstraintViolationWithTemplate(
//                                        i18nService.getMessage("error.price.max", LocaleContextHolder.getLocale())
//                                )
//                                .addPropertyNode("price")
//                                .addConstraintViolation();
//                        valid = false;
//                    }
//
//                    if(productType.getQuantity() == null) {
//                        context.disableDefaultConstraintViolation();
//                        context.buildConstraintViolationWithTemplate(
//                                        i18nService.getMessage("error.not-null", LocaleContextHolder.getLocale())
//                                )
//                                .addPropertyNode("quantity")
//                                .addConstraintViolation();
//                        valid = false;
//                    } else if (productType.getQuantity() < 0) {
//                        context.disableDefaultConstraintViolation();
//                        context.buildConstraintViolationWithTemplate(
//                                        i18nService.getMessage("error.quantity.negative", LocaleContextHolder.getLocale())
//                                )
//                                .addPropertyNode("price")
//                                .addConstraintViolation();
//                        valid = false;
//                    } else if (productType.getQuantity() > Integer.MAX_VALUE) {
//                        context.disableDefaultConstraintViolation();
//                        context.buildConstraintViolationWithTemplate(
//                                        i18nService.getMessage("error.quantity.max", LocaleContextHolder.getLocale())
//                                )
//                                .addPropertyNode("price")
//                                .addConstraintViolation();
//                        valid = false;
//                    }
//                }
//
//                return valid;
//            }
//        }

        return valid;
    }
}