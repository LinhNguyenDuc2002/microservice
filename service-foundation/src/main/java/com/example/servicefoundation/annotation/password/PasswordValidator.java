package com.example.servicefoundation.annotation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private int min;
    private int max;
    private boolean requireUpperCase;
    private boolean requireLowerCase;
    private boolean requireDigit;
    private boolean requireSpecialChar;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.requireUpperCase = constraintAnnotation.requireUpperCase();
        this.requireLowerCase = constraintAnnotation.requireLowerCase();
        this.requireDigit = constraintAnnotation.requireDigit();
        this.requireSpecialChar = constraintAnnotation.requireSpecialChar();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.hasText(password)) {
            return false;
        }

        if (password.length() < min || password.length() > max) {
            return false;
        }

        boolean hasUpperCase = !requireUpperCase || password.matches(".*[A-Z].*");
        boolean hasLowerCase = !requireLowerCase || password.matches(".*[a-z].*");
        boolean hasDigit = !requireDigit || password.matches(".*\\d.*");
        boolean hasSpecialChar = !requireSpecialChar || password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
}
