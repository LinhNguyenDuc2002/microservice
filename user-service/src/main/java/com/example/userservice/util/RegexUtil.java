package com.example.userservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegexUtil {
    public static final String REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static boolean validatePassword(String password) {
        return StringUtils.hasText(password) && password.matches(REGEX_PASSWORD);
    }

    public static boolean validateEmail(String email) {
        return StringUtils.hasText(email) && email.matches(REGEX_EMAIL);
    }
}
