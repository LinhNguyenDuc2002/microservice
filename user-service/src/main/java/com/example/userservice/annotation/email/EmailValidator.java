package com.example.userservice.annotation.email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})*$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.hasText(email)) {
            return false;
        }

        return EMAIL_PATTERN.matcher(email).matches();
    }
}
